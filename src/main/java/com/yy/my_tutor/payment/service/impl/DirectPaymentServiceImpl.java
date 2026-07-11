package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.handler.SubscriptionUpsertHelper;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.service.DirectPaymentService;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.BeneficiaryValidator;
import com.yy.my_tutor.payment.util.OrderNoGenerator;
import com.yy.my_tutor.payment.util.PaymentException;
import com.yy.my_tutor.payment.util.PaymentUserRoleUtil;
import com.yy.my_tutor.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DirectPaymentServiceImpl implements DirectPaymentService {

    @Resource private PaymentPriceMapper priceMapper;
    @Resource private PaymentProductMapper productMapper;
    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentMethodService paymentMethodService;
    @Resource private StripeClientService stripeClient;
    @Resource private BeneficiaryValidator beneficiaryValidator;
    @Resource private OrderNoGenerator orderNoGenerator;
    @Resource private StripeConfig stripeConfig;
    @Resource private SubscriptionUpsertHelper subscriptionUpsertHelper;

    public DirectPaymentServiceImpl() {
    }

    DirectPaymentServiceImpl(PaymentPriceMapper priceMapper,
                             PaymentProductMapper productMapper,
                             PaymentOrderMapper orderMapper,
                             PaymentMethodService paymentMethodService,
                             StripeClientService stripeClient,
                             BeneficiaryValidator beneficiaryValidator,
                             OrderNoGenerator orderNoGenerator,
                             StripeConfig stripeConfig) {
        this.priceMapper = priceMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.paymentMethodService = paymentMethodService;
        this.stripeClient = stripeClient;
        this.beneficiaryValidator = beneficiaryValidator;
        this.orderNoGenerator = orderNoGenerator;
        this.stripeConfig = stripeConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectPaymentResponse pay(CreateCheckoutRequest req, Integer payerUserId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return pay(req, payer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectPaymentResponse pay(CreateCheckoutRequest req, User payer) {
        ProductContext ctx = validateContext(req, payer.getId());
        if (ctx.price.getBillingInterval() != null) {
            throw PaymentException.of("PAYMENT_PRICE_REQUIRES_SUBSCRIPTION");
        }
        PaymentUserPaymentMethod method = paymentMethodService.defaultMethod(payer);
        String orderNo = orderNoGenerator.generate("ORD");
        Map<String, String> metadata = metadata(orderNo, payer.getId(), req, ctx.product);
        metadata.put("payer_role", PaymentUserRoleUtil.roleOf(payer));

        try {
            PaymentIntent pi = stripeClient.createAndConfirmPaymentIntent(
                    method.getStripeCustomerId(),
                    method.getStripePaymentMethodId(),
                    ctx.price.getUnitAmount(),
                    ctx.price.getCurrency(),
                    metadata);

            PaymentOrder order = buildOrder(orderNo, payer, req, ctx);
            order.setStripePaymentMethodId(method.getStripePaymentMethodId());
            order.setStripePaymentIntentId(pi.getId());
            order.setStatus(isSucceeded(pi.getStatus()) ? OrderStatus.PAID.name() : OrderStatus.PENDING.name());
            if (isSucceeded(pi.getStatus())) order.setPaidAt(new Date());
            orderMapper.insert(order);

            return DirectPaymentResponse.of(orderNo, pi.getId(), null, pi.getStatus(),
                    pi.getClientSecret(), requiresAction(pi.getStatus()));
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectPaymentResponse createSubscription(CreateCheckoutRequest req, Integer payerUserId) {
        User payer = new User();
        payer.setId(payerUserId);
        payer.setRole("S");
        return createSubscription(req, payer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirectPaymentResponse createSubscription(CreateCheckoutRequest req, User payer) {
        ProductContext ctx = validateContext(req, payer.getId());
        if (ctx.price.getBillingInterval() == null) {
            throw PaymentException.of("PAYMENT_PRICE_NOT_SUBSCRIPTION");
        }
        PaymentUserPaymentMethod method = paymentMethodService.defaultMethod(payer);
        String orderNo = orderNoGenerator.generate("SUB");
        Map<String, String> metadata = metadata(orderNo, payer.getId(), req, ctx.product);
        metadata.put("payer_role", PaymentUserRoleUtil.roleOf(payer));

        try {
            Subscription sub = stripeClient.createSubscription(
                    method.getStripeCustomerId(),
                    method.getStripePaymentMethodId(),
                    ctx.price.getStripePriceId(),
                    metadata);
            PaymentSubscription localSub = subscriptionUpsertHelper == null
                    ? null
                    : subscriptionUpsertHelper.upsert(sub, System.currentTimeMillis() / 1000L, metadata);
            PaymentIntent pi = latestInvoicePaymentIntent(sub);

            PaymentOrder order = buildOrder(orderNo, payer, req, ctx);
            order.setStripePaymentMethodId(method.getStripePaymentMethodId());
            order.setStripePaymentIntentId(pi == null ? null : pi.getId());
            order.setStripeInvoiceId(sub.getLatestInvoice());
            order.setSubscriptionId(localSub == null ? null : localSub.getId());
            order.setStatus(pi != null && isSucceeded(pi.getStatus()) ? OrderStatus.PAID.name() : OrderStatus.PENDING.name());
            if (OrderStatus.PAID.name().equals(order.getStatus())) order.setPaidAt(new Date());
            orderMapper.insert(order);

            return DirectPaymentResponse.of(orderNo, pi == null ? null : pi.getId(),
                    localSub == null ? null : localSub.getId(),
                    pi == null ? sub.getStatus() : pi.getStatus(),
                    pi == null ? null : pi.getClientSecret(),
                    pi != null && requiresAction(pi.getStatus()));
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    private ProductContext validateContext(CreateCheckoutRequest req, Integer payerUserId) {
        PaymentPrice price = priceMapper.selectById(req.getPriceId());
        if (price == null || price.getStatus() == null || price.getStatus() != 1) {
            throw PaymentException.of("PAYMENT_PRICE_NOT_FOUND", "Price not found or unavailable.");
        }
        PaymentProduct product = productMapper.selectById(price.getProductId());
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            throw PaymentException.of("PAYMENT_PRODUCT_NOT_FOUND", "Product not found or unavailable.");
        }
        if (!stripeConfig.isLocalAuthBypassSkipBeneficiaryValidation()) {
            beneficiaryValidator.assertAccessible(payerUserId, req.getBeneficiaryStudentId());
        }
        return new ProductContext(price, product);
    }

    private PaymentOrder buildOrder(String orderNo, User payer, CreateCheckoutRequest req, ProductContext ctx) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNo);
        order.setPayerUserId(payer.getId());
        order.setPayerRole(PaymentUserRoleUtil.roleOf(payer));
        order.setBeneficiaryStudentId(req.getBeneficiaryStudentId());
        order.setProductId(ctx.product.getId());
        order.setPriceId(ctx.price.getId());
        order.setQuantity(1);
        order.setUnitAmount(ctx.price.getUnitAmount());
        order.setCurrency(ctx.price.getCurrency());
        order.setAmount(ctx.price.getUnitAmount());
        order.setCreateBy(String.valueOf(payer.getId()));
        order.setUpdateBy(String.valueOf(payer.getId()));
        return order;
    }

    private Map<String, String> metadata(String orderNo, Integer payerUserId,
                                         CreateCheckoutRequest req, PaymentProduct product) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("local_order_no", orderNo);
        metadata.put("payer_user_id", String.valueOf(payerUserId));
        metadata.put("beneficiary_student_id", String.valueOf(req.getBeneficiaryStudentId()));
        metadata.put("product_type", product.getProductType());
        if (product.getTargetRefId() != null) metadata.put("target_ref_id", String.valueOf(product.getTargetRefId()));
        return metadata;
    }

    private PaymentIntent latestInvoicePaymentIntent(Subscription sub) {
        Invoice invoice = sub.getLatestInvoiceObject();
        return invoice == null ? null : invoice.getPaymentIntentObject();
    }

    private boolean isSucceeded(String status) {
        return "succeeded".equals(status);
    }

    private boolean requiresAction(String status) {
        return "requires_action".equals(status);
    }

    private static class ProductContext {
        private final PaymentPrice price;
        private final PaymentProduct product;

        private ProductContext(PaymentPrice price, PaymentProduct product) {
            this.price = price;
            this.product = product;
        }
    }
}
