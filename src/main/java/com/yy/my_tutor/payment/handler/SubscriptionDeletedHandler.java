package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class SubscriptionDeletedHandler implements EventHandler {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "customer.subscription.deleted"; }

    @Override
    public HandlerResult handle(Event event) {
        Subscription remote = EventObjectExtractor.get(event, Subscription.class);
        if (remote == null) return HandlerResult.FAILED;
        PaymentSubscription local = subscriptionMapper.selectByStripeId(remote.getId());
        if (local == null) return HandlerResult.SKIPPED;
        local.setStatus("canceled");
        if (remote.getEndedAt() != null) local.setEndedAt(new Date(remote.getEndedAt() * 1000L));
        if (remote.getCanceledAt() != null) local.setCanceledAt(new Date(remote.getCanceledAt() * 1000L));
        subscriptionMapper.updateById(local);
        cacheInvalidator.invalidate(local.getBeneficiaryStudentId());
        log.info("Subscription {} → canceled", remote.getId());
        return HandlerResult.SUCCESS;
    }
}
