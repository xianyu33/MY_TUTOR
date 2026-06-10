package com.yy.my_tutor.payment.handler;

import com.stripe.model.Subscription;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class SubscriptionUpsertHelper {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private PaymentPriceMapper priceMapper;

    public PaymentSubscription upsert(Subscription remote, long eventCreatedEpoch, Map<String, String> metadata) {
        PaymentSubscription local = subscriptionMapper.selectByStripeId(remote.getId());
        if (local == null) {
            local = new PaymentSubscription();
            local.setStripeSubscriptionId(remote.getId());
            local.setPayerUserId(parseInt(metadata, "payer_user_id"));
            local.setBeneficiaryStudentId(parseInt(metadata, "beneficiary_student_id"));
            String stripePriceId = extractStripePriceId(remote);
            if (stripePriceId != null) {
                PaymentPrice p = priceMapper.selectByStripeId(stripePriceId);
                if (p != null) {
                    local.setPriceId(p.getId());
                    local.setProductId(p.getProductId());
                    local.setCurrency(p.getCurrency());
                }
            }
            applyRemote(local, remote);
            subscriptionMapper.insert(local);
            log.info("Subscription inserted: stripeId={}, status={}", remote.getId(), remote.getStatus());
            return local;
        }
        long localUpdateEpoch = local.getUpdateAt() == null ? 0 : local.getUpdateAt().getTime() / 1000;
        if (eventCreatedEpoch < localUpdateEpoch) {
            log.info("Subscription event stale: stripeId={}, event.created={} < local.update_at={}, skip",
                    remote.getId(), eventCreatedEpoch, localUpdateEpoch);
            return null;
        }
        applyRemote(local, remote);
        subscriptionMapper.updateById(local);
        return local;
    }

    private void applyRemote(PaymentSubscription local, Subscription remote) {
        local.setStatus(remote.getStatus());
        if (remote.getCurrentPeriodStart() != null)
            local.setCurrentPeriodStart(new Date(remote.getCurrentPeriodStart() * 1000L));
        if (remote.getCurrentPeriodEnd() != null)
            local.setCurrentPeriodEnd(new Date(remote.getCurrentPeriodEnd() * 1000L));
        local.setCancelAtPeriodEnd(Boolean.TRUE.equals(remote.getCancelAtPeriodEnd()) ? 1 : 0);
        if (remote.getCanceledAt() != null) local.setCanceledAt(new Date(remote.getCanceledAt() * 1000L));
        if (remote.getEndedAt() != null) local.setEndedAt(new Date(remote.getEndedAt() * 1000L));
        local.setLatestInvoiceId(remote.getLatestInvoice());
    }

    private static String extractStripePriceId(Subscription sub) {
        if (sub.getItems() == null || sub.getItems().getData() == null || sub.getItems().getData().isEmpty()) return null;
        return sub.getItems().getData().get(0).getPrice().getId();
    }

    private static Integer parseInt(Map<String, String> meta, String key) {
        if (meta == null) return null;
        String v = meta.get(key);
        if (v == null) return null;
        try { return Integer.valueOf(v); } catch (NumberFormatException e) { return null; }
    }
}
