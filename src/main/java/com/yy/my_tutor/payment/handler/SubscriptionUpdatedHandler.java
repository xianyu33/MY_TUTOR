package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SubscriptionUpdatedHandler implements EventHandler {

    @Resource private SubscriptionUpsertHelper upsertHelper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "customer.subscription.updated"; }

    @Override
    public HandlerResult handle(Event event) {
        Subscription sub = EventObjectExtractor.get(event, Subscription.class);
        if (sub == null) return HandlerResult.FAILED;
        PaymentSubscription local = upsertHelper.upsert(sub, event.getCreated(), sub.getMetadata(), event);
        if (local == null) return HandlerResult.SKIPPED;
        cacheInvalidator.invalidate(local.getBeneficiaryStudentId());
        return HandlerResult.SUCCESS;
    }
}
