package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.my_tutor.payment.domain.dto.AnnualLicenseQuoteDTO;
import com.yy.my_tutor.payment.domain.dto.AnnualLicenseSeatUsageDTO;
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
import com.yy.my_tutor.user.domain.StudentDetailDTO;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AnnualLicenseServiceImpl implements AnnualLicenseService {

    private static final String CURRENCY_CAD = "cad";
    private static final String LEDGER_PURCHASE = "PURCHASE";
    private static final String LEDGER_ACTIVATE = "ACTIVATE";
    private static final String LICENSE_UNACTIVATED = "UNACTIVATED";
    private static final String LICENSE_ACTIVE = "ACTIVE";
    private static final String LICENSE_EXPIRED = "EXPIRED";

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
            throw PaymentException.of("PAYMENT_PRODUCT_REQUIRED", "Product is required.");
        }
        if (quantity == null || quantity <= 0) {
            throw PaymentException.of("PAYMENT_QUANTITY_INVALID", "Quantity must be greater than 0.");
        }

        PaymentProduct product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == null || product.getStatus() != 1
                || !ProductType.ANNUAL_LICENSE.name().equals(product.getProductType())) {
            throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND", "Annual membership product not found or unavailable.");
        }

        PaymentPrice price = annualPrice(productId);
        PaymentPriceTier tier = tierMapper.selectActiveTier(price.getId(), quantity);
        if (tier == null) {
            throw PaymentException.of("PAYMENT_PRICE_TIER_NOT_FOUND", "No valid tier price found for the selected quantity.");
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
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "Please log in first.");
        }
        Integer quantity = req.getQuantity() == null ? 1 : req.getQuantity();
        boolean student = "S".equals(payer.getRole());
        boolean teacher = payer.getType() != null && payer.getType() == 1;
        if (student && quantity != 1) {
            throw PaymentException.of("PAYMENT_STUDENT_QUANTITY_INVALID", "Students can only purchase 1 annual membership.");
        }
        if (!student && !teacher) {
            throw PaymentException.of("PAYMENT_ROLE_INVALID", "Only students or teachers can purchase annual membership.");
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
            throw PaymentException.of("PAYMENT_ACTIVATION_INVALID", "Teacher and student are required.");
        }
        if (seatLedgerMapper.countActivation(teacherId, studentId) > 0) {
            log.info("Teacher {} already activated student {}, skip duplicate activation", teacherId, studentId);
            return false;
        }
        if (seatLedgerMapper.availableSeats(teacherId) <= 0) {
            throw PaymentException.of("PAYMENT_TEACHER_SEAT_NOT_ENOUGH", "Insufficient available memberships for the teacher.");
        }
        Integer orderId = findConsumableSeatOrderId(teacherId);
        TeacherSeatLedger ledger = new TeacherSeatLedger();
        ledger.setTeacherId(teacherId);
        ledger.setOrderId(orderId);
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

    @Override
    public void enrichTeacherStudentLicense(Integer teacherId, List<StudentDetailDTO> students) {
        if (students == null || students.isEmpty()) {
            return;
        }
        Map<Integer, StudentDetailDTO> studentById = new HashMap<>();
        for (StudentDetailDTO student : students) {
            if (student == null || student.getStudentId() == null) {
                continue;
            }
            student.setLicenseActivated(false);
            student.setLicenseStatus(LICENSE_UNACTIVATED);
            studentById.put(student.getStudentId(), student);
        }
        if (studentById.isEmpty()) {
            return;
        }

        QueryWrapper<TeacherSeatLedger> query = new QueryWrapper<>();
        query.eq("teacher_id", teacherId)
                .eq("type", LEDGER_ACTIVATE)
                .eq("delete_flag", "N")
                .in("student_id", studentById.keySet())
                .orderByDesc("create_at")
                .orderByDesc("id");
        List<TeacherSeatLedger> ledgers = seatLedgerMapper.selectList(query);
        if (ledgers == null || ledgers.isEmpty()) {
            return;
        }

        Date now = new Date();
        for (TeacherSeatLedger ledger : ledgers) {
            if (ledger == null || ledger.getStudentId() == null) {
                continue;
            }
            StudentDetailDTO detail = studentById.remove(ledger.getStudentId());
            if (detail == null) {
                continue;
            }
            User student = userMapper.findById(ledger.getStudentId());
            Date expireAt = student == null ? null : student.getExpireTime();
            detail.setLicenseExpireAt(expireAt);
            detail.setLicenseActivated(expireAt != null && expireAt.after(now));
            detail.setLicenseStatus(Boolean.TRUE.equals(detail.getLicenseActivated())
                    ? LICENSE_ACTIVE : LICENSE_EXPIRED);
            if (ledger.getOrderId() != null) {
                PaymentOrder order = orderMapper.selectById(ledger.getOrderId());
                if (order != null) {
                    detail.setActivatedOrderNo(order.getOrderNo());
                }
            }
        }
    }

    @Override
    public AnnualLicenseSeatUsageDTO seatUsage(User teacher) {
        if (teacher == null || teacher.getId() == null) {
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "Please log in first.");
        }
        if (teacher.getType() == null || teacher.getType() != 1) {
            throw PaymentException.of("PAYMENT_TEACHER_REQUIRED", "Only teachers can view annual membership usage.");
        }

        QueryWrapper<TeacherSeatLedger> query = new QueryWrapper<>();
        query.eq("teacher_id", teacher.getId())
                .eq("delete_flag", "N")
                .isNotNull("order_id")
                .orderByAsc("create_at")
                .orderByAsc("id");
        List<TeacherSeatLedger> ledgers = seatLedgerMapper.selectList(query);

        AnnualLicenseSeatUsageDTO usage = new AnnualLicenseSeatUsageDTO();
        usage.setTotalSeats(0);
        usage.setUsedSeats(0);
        usage.setAvailableSeats(0);
        Map<Integer, AnnualLicenseSeatUsageDTO.OrderUsageDTO> usageByOrder = new LinkedHashMap<>();
        if (ledgers == null || ledgers.isEmpty()) {
            return usage;
        }

        for (TeacherSeatLedger ledger : ledgers) {
            if (ledger == null || ledger.getOrderId() == null || ledger.getChangeCount() == null) {
                continue;
            }
            AnnualLicenseSeatUsageDTO.OrderUsageDTO orderUsage =
                    getOrCreateOrderUsage(usageByOrder, ledger.getOrderId());
            if (LEDGER_PURCHASE.equals(ledger.getType())) {
                int quantity = Math.max(ledger.getChangeCount(), 0);
                orderUsage.setQuantity(orderUsage.getQuantity() + quantity);
                usage.setTotalSeats(usage.getTotalSeats() + quantity);
            } else if (LEDGER_ACTIVATE.equals(ledger.getType())) {
                int used = Math.max(-ledger.getChangeCount(), 0);
                orderUsage.setUsed(orderUsage.getUsed() + used);
                usage.setUsedSeats(usage.getUsedSeats() + used);
                AnnualLicenseSeatUsageDTO.ActivatedStudentDTO activatedStudent =
                        buildActivatedStudent(ledger);
                orderUsage.getActivatedStudents().add(activatedStudent);
            }
        }

        for (AnnualLicenseSeatUsageDTO.OrderUsageDTO orderUsage : usageByOrder.values()) {
            orderUsage.setAvailable(Math.max(orderUsage.getQuantity() - orderUsage.getUsed(), 0));
            usage.getOrders().add(orderUsage);
        }
        usage.setAvailableSeats(Math.max(usage.getTotalSeats() - usage.getUsedSeats(), 0));
        return usage;
    }

    private AnnualLicenseSeatUsageDTO.OrderUsageDTO getOrCreateOrderUsage(
            Map<Integer, AnnualLicenseSeatUsageDTO.OrderUsageDTO> usageByOrder,
            Integer orderId) {
        AnnualLicenseSeatUsageDTO.OrderUsageDTO existing = usageByOrder.get(orderId);
        if (existing != null) {
            return existing;
        }
        AnnualLicenseSeatUsageDTO.OrderUsageDTO orderUsage = new AnnualLicenseSeatUsageDTO.OrderUsageDTO();
        orderUsage.setOrderId(orderId);
        orderUsage.setQuantity(0);
        orderUsage.setUsed(0);
        orderUsage.setAvailable(0);
        PaymentOrder order = orderMapper.selectById(orderId);
        if (order != null) {
            orderUsage.setOrderNo(order.getOrderNo());
            orderUsage.setPaidAt(order.getPaidAt());
            orderUsage.setUnitAmount(order.getUnitAmount());
            orderUsage.setCurrency(order.getCurrency());
            if (order.getProductId() != null) {
                PaymentProduct product = productMapper.selectById(order.getProductId());
                if (product != null) {
                    orderUsage.setProductName(product.getName());
                }
            }
        }
        usageByOrder.put(orderId, orderUsage);
        return orderUsage;
    }

    private AnnualLicenseSeatUsageDTO.ActivatedStudentDTO buildActivatedStudent(TeacherSeatLedger ledger) {
        AnnualLicenseSeatUsageDTO.ActivatedStudentDTO dto =
                new AnnualLicenseSeatUsageDTO.ActivatedStudentDTO();
        dto.setStudentId(ledger.getStudentId());
        dto.setActivatedAt(ledger.getCreateAt());
        if (ledger.getStudentId() != null) {
            User student = userMapper.findById(ledger.getStudentId());
            if (student != null) {
                dto.setStudentName(student.getUsername());
                dto.setStudentAccount(student.getUserAccount());
                dto.setExpireAt(student.getExpireTime());
            }
        }
        return dto;
    }

    private Integer findConsumableSeatOrderId(Integer teacherId) {
        QueryWrapper<TeacherSeatLedger> query = new QueryWrapper<>();
        query.eq("teacher_id", teacherId)
                .eq("delete_flag", "N")
                .isNotNull("order_id")
                .orderByAsc("create_at")
                .orderByAsc("id");
        List<TeacherSeatLedger> ledgers = seatLedgerMapper.selectList(query);

        Map<Integer, Integer> remainingByOrder = new LinkedHashMap<>();
        if (ledgers != null) {
            for (TeacherSeatLedger ledger : ledgers) {
                if (ledger == null || ledger.getOrderId() == null || ledger.getChangeCount() == null) {
                    continue;
                }
                Integer current = remainingByOrder.get(ledger.getOrderId());
                remainingByOrder.put(ledger.getOrderId(),
                        (current == null ? 0 : current) + ledger.getChangeCount());
            }
        }
        for (Map.Entry<Integer, Integer> entry : remainingByOrder.entrySet()) {
            if (entry.getValue() != null && entry.getValue() > 0) {
                return entry.getKey();
            }
        }
        throw PaymentException.of("PAYMENT_TEACHER_SEAT_ORDER_NOT_FOUND", "No consumable teacher membership order found.");
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
        throw PaymentException.of("PAYMENT_PRICE_NOT_FOUND", "Annual membership CAD one-time price not found or unavailable.");
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
            throw PaymentException.of("PAYMENT_STUDENT_NOT_FOUND", "Student not found. Unable to activate annual membership.");
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
