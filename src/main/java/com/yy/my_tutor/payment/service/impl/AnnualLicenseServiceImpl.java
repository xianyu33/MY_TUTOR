package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.yy.my_tutor.payment.domain.dto.AnnualLicenseQuoteDTO;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentPriceTier;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import com.yy.my_tutor.payment.domain.entity.TeacherSeatLedger;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.domain.enums.ProductType;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentPriceTierMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.mapper.TeacherSeatLedgerMapper;
import com.yy.my_tutor.payment.service.AnnualLicenseService;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.OrderNoGenerator;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.payment.util.PaymentUserRoleUtil;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AnnualLicenseServiceImpl implements AnnualLicenseService {

    private static final String CURRENCY_CAD = "cad";
    private static final String LEDGER_PURCHASE = "PURCHASE";
    private static final String LEDGER_ACTIVATE = "ACTIVATE";

    @Resource private PaymentProductMapper productMapper;
    @Resource private PaymentPriceMapper priceMapper;
    @Resource private PaymentPriceTierMapper tierMapper;
    @Resource private PaymentMethodService paymentMethodService;
    @Resource private StripeClientService stripeClient;
    @Resource private OrderNoGenerator orderNoGenerator;
    @Resource private PaymentOrderMapper orderMapper;
    @Resource private TeacherSeatLedgerMapper seatLedgerMapper;
    @Resource private UserMapper userMapper;

    @Override
    public boolean supports(CreateCheckoutRequest req) {
        return req != null && (req.getProductId() != null || req.getQuantity() != null);
    }

    @Override
    public AnnualLicenseQuoteDTO quote(Integer productId, Integer quantity) {
        if (productId == null) {
            throw PaymentException.of("PAYMENT_PRODUCT_REQUIRED", "商品不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw PaymentException.of("PAYMENT_QUANTITY_INVALID", "购买数量必须大于 0");
        }

        PaymentProduct product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == null || product.getStatus() != 1
                || !ProductType.ANNUAL_LICENSE.name().equals(product.getProductType())) {
            throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND", "年度授权商品不存在或已下架");
        }

        PaymentPrice price = annualPrice(productId);
        PaymentPriceTier tier = tierMapper.selectActiveTier(price.getId(), quantity);
        if (tier == null) {
            throw PaymentException.of("PAYMENT_PRICE_TIER_NOT_FOUND", "购买数量未匹配到有效阶梯价格");
        }

        AnnualLicenseQuoteDTO quote = new AnnualLicenseQuoteDTO();
        quote.setProductId(productId);
        quote.setPriceId(price.getId());
        quote.setPriceTierId(tier.getId());
        quote.setCurrency(price.getCurrency());
        quote.setQuantity(quantity);
        quote.setUnitAmount(tier.getUnitAmount());
        quote.setTotalAmount(tier.getUnitAmount() * quantity);
        return quote;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectPaymentResponse pay(CreateCheckoutRequest req, User payer) {
        if (payer == null || payer.getId() == null) {
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "请先登录");
        }
        Integer quantity = req.getQuantity() == null ? 1 : req.getQuantity();
        boolean student = "S".equals(payer.getRole());
        boolean teacher = payer.getType() != null && payer.getType() == 1;
        if (student && quantity != 1) {
            throw PaymentException.of("PAYMENT_STUDENT_QUANTITY_INVALID", "学生只能购买 1 个年度授权");
        }
        if (!student && !teacher) {
            throw PaymentException.of("PAYMENT_ROLE_INVALID", "仅学生或老师可以购买年度授权");
        }

        AnnualLicenseQuoteDTO quote = quote(req.getProductId(), quantity);
        PaymentUserPaymentMethod method = paymentMethodService.defaultMethod(payer);
        String orderNo = orderNoGenerator.generate(student ? "ANN-STU" : "ANN-TCH");
        Map<String, String> metadata = metadata(orderNo, payer, quote);

        try {
            PaymentIntent pi = stripeClient.createAndConfirmPaymentIntent(
                    method.getStripeCustomerId(),
                    method.getStripePaymentMethodId(),
                    quote.getTotalAmount(),
                    quote.getCurrency(),
                    metadata);
            PaymentOrder order = buildOrder(orderNo, payer, quote, student ? payer.getId() : null, pi, method);
            orderMapper.insert(order);
            if (OrderStatus.PAID.name().equals(order.getStatus())) {
                if (student) {
                    activateStudent(payer.getId(), "PAYMENT_ORDER:" + orderNo);
                } else {
                    addTeacherSeats(payer.getId(), order.getId(), quote.getQuantity(), "PAYMENT_ORDER:" + orderNo);
                }
            }
            return DirectPaymentResponse.of(orderNo, pi.getId(), null, pi.getStatus(),
                    pi.getClientSecret(), requiresAction(pi.getStatus()));
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateForTeacherBind(Integer teacherId, Integer studentId, String operator) {
        if (teacherId == null || studentId == null) {
            throw PaymentException.of("PAYMENT_ACTIVATION_INVALID", "老师和学生不能为空");
        }
        if (seatLedgerMapper.countActivation(teacherId, studentId) > 0) {
            log.info("Teacher {} already activated student {}, skip duplicate activation", teacherId, studentId);
            return false;
        }
        if (seatLedgerMapper.availableSeats(teacherId) <= 0) {
            throw PaymentException.of("PAYMENT_TEACHER_SEAT_NOT_ENOUGH", "老师可用名额不足");
        }
        TeacherSeatLedger ledger = new TeacherSeatLedger();
        ledger.setTeacherId(teacherId);
        ledger.setStudentId(studentId);
        ledger.setChangeCount(-1);
        ledger.setType(LEDGER_ACTIVATE);
        ledger.setCreateBy(operator);
        ledger.setUpdateBy(operator);
        ledger.setDeleteFlag("N");
        seatLedgerMapper.insert(ledger);
        activateStudent(studentId, operator);
        return true;
    }

    private PaymentPrice annualPrice(Integer productId) {
        List<PaymentPrice> prices = priceMapper.selectActiveByProductId(productId);
        if (prices != null) {
            for (PaymentPrice price : prices) {
                if (price != null
                        && CURRENCY_CAD.equalsIgnoreCase(price.getCurrency())
                        && !StringUtils.hasText(price.getBillingInterval())) {
                    return price;
                }
            }
        }
        throw PaymentException.of("PAYMENT_PRICE_NOT_FOUND", "年度授权 CAD 一次性价格不存在或已下架");
    }

    private PaymentOrder buildOrder(String orderNo, User payer, AnnualLicenseQuoteDTO quote,
                                    Integer beneficiaryStudentId, PaymentIntent pi,
                                    PaymentUserPaymentMethod method) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNo);
        order.setPayerUserId(payer.getId());
        order.setPayerRole(PaymentUserRoleUtil.roleOf(payer));
        order.setBeneficiaryStudentId(beneficiaryStudentId);
        order.setProductId(quote.getProductId());
        order.setPriceId(quote.getPriceId());
        order.setPriceTierId(quote.getPriceTierId());
        order.setQuantity(quote.getQuantity());
        order.setUnitAmount(quote.getUnitAmount());
        order.setCurrency(quote.getCurrency());
        order.setAmount(quote.getTotalAmount());
        order.setStripePaymentMethodId(method.getStripePaymentMethodId());
        order.setStripePaymentIntentId(pi.getId());
        order.setStatus(isSucceeded(pi.getStatus()) ? OrderStatus.PAID.name() : OrderStatus.PENDING.name());
        if (OrderStatus.PAID.name().equals(order.getStatus())) {
            order.setPaidAt(new java.util.Date());
        }
        order.setCreateBy(String.valueOf(payer.getId()));
        order.setUpdateBy(String.valueOf(payer.getId()));
        return order;
    }

    private void addTeacherSeats(Integer teacherId, Integer orderId, Integer quantity, String operator) {
        TeacherSeatLedger ledger = new TeacherSeatLedger();
        ledger.setTeacherId(teacherId);
        ledger.setOrderId(orderId);
        ledger.setChangeCount(quantity);
        ledger.setType(LEDGER_PURCHASE);
        ledger.setCreateBy(operator);
        ledger.setUpdateBy(operator);
        ledger.setDeleteFlag("N");
        seatLedgerMapper.insert(ledger);
    }

    private void activateStudent(Integer studentId, String updateBy) {
        int updated = userMapper.setExpireTimeOneYearFromNow(studentId, updateBy);
        if (updated == 0) {
            throw PaymentException.of("PAYMENT_STUDENT_NOT_FOUND", "学生不存在,无法激活年度授权");
        }
    }

    private Map<String, String> metadata(String orderNo, User payer, AnnualLicenseQuoteDTO quote) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("local_order_no", orderNo);
        metadata.put("annual_license", "true");
        metadata.put("payer_user_id", String.valueOf(payer.getId()));
        metadata.put("payer_role", payer.getRole() == null ? "" : payer.getRole());
        metadata.put("product_id", String.valueOf(quote.getProductId()));
        metadata.put("price_id", String.valueOf(quote.getPriceId()));
        metadata.put("price_tier_id", String.valueOf(quote.getPriceTierId()));
        metadata.put("quantity", String.valueOf(quote.getQuantity()));
        metadata.put("unit_amount", String.valueOf(quote.getUnitAmount()));
        metadata.put("total_amount", String.valueOf(quote.getTotalAmount()));
        return metadata;
    }

    private boolean isSucceeded(String status) {
        return "succeeded".equals(status);
    }

    private boolean requiresAction(String status) {
        return "requires_action".equals(status);
    }
}
