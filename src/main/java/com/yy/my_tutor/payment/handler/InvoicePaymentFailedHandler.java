package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class InvoicePaymentFailedHandler implements EventHandler {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "invoice.payment_failed"; }

    @Override
    public HandlerResult handle(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);
        if (invoice == null || invoice.getSubscription() == null) return HandlerResult.SKIPPED;
        PaymentSubscription sub = subscriptionMapper.selectByStripeId(invoice.getSubscription());
        if (sub == null) return HandlerResult.SKIPPED;
        sub.setStatus("past_due");
        subscriptionMapper.updateById(sub);
        cacheInvalidator.invalidate(sub.getBeneficiaryStudentId());
        log.warn("Subscription {} → past_due", sub.getStripeSubscriptionId());
        return HandlerResult.SUCCESS;
    }
}
