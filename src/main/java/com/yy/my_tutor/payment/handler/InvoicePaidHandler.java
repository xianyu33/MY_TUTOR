package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import com.yy.my_tutor.payment.util.OrderNoGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class InvoicePaidHandler implements EventHandler {

    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private OrderNoGenerator orderNoGenerator;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "invoice.paid"; }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HandlerResult handle(Event event) {
        Invoice invoice = EventObjectExtractor.get(event, Invoice.class);
        if (invoice == null) return HandlerResult.FAILED;
        if ("subscription_create".equals(invoice.getBillingReason())) return HandlerResult.SKIPPED;
        PaymentOrder exists = orderMapper.selectByInvoiceId(invoice.getId());
        if (exists != null) return HandlerResult.SKIPPED;
        if (invoice.getSubscription() == null) return HandlerResult.SKIPPED;

        PaymentSubscription sub = subscriptionMapper.selectByStripeId(invoice.getSubscription());
        if (sub == null) {
            log.error("invoice.paid: local subscription not found for {}", invoice.getSubscription());
            return HandlerResult.FAILED;
        }

        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNoGenerator.generate("RENEW"));
        order.setPayerUserId(sub.getPayerUserId());
        order.setBeneficiaryStudentId(sub.getBeneficiaryStudentId());
        order.setProductId(sub.getProductId());
        order.setPriceId(sub.getPriceId());
        order.setQuantity(1);
        order.setUnitAmount(invoice.getAmountPaid());
        order.setCurrency(invoice.getCurrency());
        order.setAmount(invoice.getAmountPaid());
        order.setStatus(OrderStatus.PAID.name());
        order.setStripeInvoiceId(invoice.getId());
        order.setStripePaymentIntentId(invoice.getPaymentIntent());
        order.setSubscriptionId(sub.getId());
        order.setPaidAt(invoice.getStatusTransitions() != null && invoice.getStatusTransitions().getPaidAt() != null
                ? new Date(invoice.getStatusTransitions().getPaidAt() * 1000L)
                : new Date(event.getCreated() * 1000L));
        order.setCreateBy("STRIPE_WEBHOOK");
        order.setUpdateBy("STRIPE_WEBHOOK");
        orderMapper.insert(order);

        cacheInvalidator.invalidate(sub.getBeneficiaryStudentId());
        log.info("Renewal order {} created for subscription {}", order.getOrderNo(), sub.getStripeSubscriptionId());
        return HandlerResult.SUCCESS;
    }
}
