package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.service.CheckoutService;
import com.yy.my_tutor.payment.service.CustomerService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.BeneficiaryValidator;
import com.yy.my_tutor.payment.util.OrderNoGenerator;
import com.yy.my_tutor.payment.util.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Resource private StripeClientService stripeClient;
    @Resource private CustomerService customerService;
    @Resource private BeneficiaryValidator beneficiaryValidator;
    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentPriceMapper priceMapper;
    @Resource private PaymentProductMapper productMapper;
    @Resource private OrderNoGenerator orderNoGenerator;
    @Resource private StripeConfig stripeConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckoutSessionResponse createSession(CreateCheckoutRequest req, Integer payerUserId) {
        // 1. 校验 price
        PaymentPrice price = priceMapper.selectById(req.getPriceId());
        if (price == null || price.getStatus() == null || price.getStatus() != 1) {
            throw PaymentException.of("PAYMENT_PRICE_NOT_FOUND", "价格不存在或已下架");
        }
        PaymentProduct product = productMapper.selectById(price.getProductId());
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND", "商品不存在或已下架");
        }

        // 2. 受益学生校验
        if (!stripeConfig.isLocalAuthBypassSkipBeneficiaryValidation()) {
            beneficiaryValidator.assertAccessible(payerUserId, req.getBeneficiaryStudentId());
        }

        // 3. 校验 return URL
        validateReturnUrl(req.getSuccessUrl());
        validateReturnUrl(req.getCancelUrl());

        // 4. Customer 懒创建
        PaymentCustomer customer = customerService.getOrCreate(payerUserId);

        // 5. 生成订单号 + 写本地 PENDING 订单
        String orderNo = orderNoGenerator.generate(price.getBillingInterval() == null ? "ORD" : "SUB");
        long expiresAtEpoch = System.currentTimeMillis() / 1000
                + TimeUnit.HOURS.toSeconds(stripeConfig.getCheckout().getSessionExpiresAfterHours());

        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNo);
        order.setPayerUserId(payerUserId);
        order.setBeneficiaryStudentId(req.getBeneficiaryStudentId());
        order.setProductId(product.getId());
        order.setPriceId(price.getId());
        order.setCurrency(price.getCurrency());
        order.setAmount(price.getUnitAmount());
        order.setStatus(OrderStatus.PENDING.name());
        order.setExpireAt(new Date(expiresAtEpoch * 1000L));
        order.setCreateBy(String.valueOf(payerUserId));
        order.setUpdateBy(String.valueOf(payerUserId));
        orderMapper.insert(order);

        // 6. 调 Stripe 创建 Session
        Map<String, String> metadata = new HashMap<>();
        metadata.put("local_order_no", orderNo);
        metadata.put("payer_user_id", String.valueOf(payerUserId));
        metadata.put("beneficiary_student_id", String.valueOf(req.getBeneficiaryStudentId()));
        metadata.put("product_type", product.getProductType());
        if (product.getTargetRefId() != null) {
            metadata.put("target_ref_id", String.valueOf(product.getTargetRefId()));
        }

        String mode = price.getBillingInterval() == null ? "payment" : "subscription";

        // success_url 必须含 {CHECKOUT_SESSION_ID} 占位符,Stripe 会替换为真实 session_id
        String successUrl = req.getSuccessUrl();
        if (!successUrl.contains("{CHECKOUT_SESSION_ID}") && !successUrl.contains("%7BCHECKOUT_SESSION_ID%7D")) {
            String sep = successUrl.contains("?") ? "&" : "?";
            successUrl = successUrl + sep + "session_id={CHECKOUT_SESSION_ID}";
        }

        Session session;
        try {
            session = stripeClient.createCheckoutSession(
                    customer.getStripeCustomerId(),
                    price.getStripePriceId(),
                    mode,
                    successUrl,
                    req.getCancelUrl(),
                    expiresAtEpoch,
                    metadata);
        } catch (StripeException e) {
            log.error("Stripe createCheckoutSession failed orderNo={}", orderNo, e);
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        // 7. 回写 session_id 到本地订单
        order.setStripeCheckoutSessionId(session.getId());
        orderMapper.updateById(order);

        return new CheckoutSessionResponse(session.getId(), session.getUrl(), orderNo);
    }

    @Override
    public CheckoutSessionResponse createSetupSession(Integer payerUserId, String returnUrl) {
        validateReturnUrl(returnUrl);
        PaymentCustomer customer = customerService.getOrCreate(payerUserId);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("payer_user_id", String.valueOf(payerUserId));
        try {
            Session session = stripeClient.createSetupSession(customer.getStripeCustomerId(), returnUrl, metadata);
            return new CheckoutSessionResponse(session.getId(), session.getUrl(), null);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    public PaymentOrder getOrderStatus(String orderNo, Integer payerUserId) {
        PaymentOrder order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) throw PaymentException.of("PAYMENT_ORDER_NOT_FOUND");
        if (!payerUserId.equals(order.getPayerUserId())) {
            throw PaymentException.of("PAYMENT_ORDER_NOT_OWNED");
        }
        return order;
    }

    private void validateReturnUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw PaymentException.of("PAYMENT_RETURN_URL_INVALID");
        }
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            if (host == null
                    || stripeConfig.getCheckout().getAllowedReturnHosts() == null
                    || !stripeConfig.getCheckout().getAllowedReturnHosts().contains(host)) {
                throw PaymentException.of("PAYMENT_RETURN_URL_INVALID", "URL host 不在白名单: " + host);
            }
        } catch (IllegalArgumentException e) {
            throw PaymentException.of("PAYMENT_RETURN_URL_INVALID", "URL 格式错误");
        }
    }
}
