package com.yy.my_tutor.payment.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.model.Event;
import com.stripe.model.Price;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class SubscriptionUpsertHelper {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private PaymentPriceMapper priceMapper;

    public PaymentSubscription upsert(Subscription remote, long eventCreatedEpoch, Map<String, String> metadata) {
        return upsert(remote, eventCreatedEpoch, metadata, null);
    }

    public PaymentSubscription upsert(Subscription remote, long eventCreatedEpoch, Map<String, String> metadata, Event event) {
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
            applyRemote(local, remote, event);
            int inserted = subscriptionMapper.insertIgnore(local);
            if (inserted > 0) {
                log.info("Subscription inserted: stripeId={}, status={}", remote.getId(), remote.getStatus());
                PaymentSubscription insertedLocal = selectExistingWithRetry(remote.getId());
                return insertedLocal == null ? local : insertedLocal;
            }

            log.info("Subscription {} already exists or was inserted concurrently, will update existing row", remote.getId());
            PaymentSubscription existing = selectExistingWithRetry(remote.getId());
            if (existing == null) {
                log.warn("Subscription {} insert was ignored but existing row is not visible yet, skip update", remote.getId());
                return null;
            }
            mergeCreateFields(existing, local);
            applyRemote(existing, remote, event);
            subscriptionMapper.updateById(existing);
            return existing;
        }
        long localUpdateEpoch = local.getUpdateAt() == null ? 0 : local.getUpdateAt().getTime() / 1000;
        if (eventCreatedEpoch < localUpdateEpoch) {
            log.info("Subscription event stale: stripeId={}, event.created={} < local.update_at={}, skip",
                    remote.getId(), eventCreatedEpoch, localUpdateEpoch);
            return null;
        }
        applyRemote(local, remote, event);
        subscriptionMapper.updateById(local);
        return local;
    }

    private void mergeCreateFields(PaymentSubscription target, PaymentSubscription source) {
        if (target.getPayerUserId() == null) target.setPayerUserId(source.getPayerUserId());
        if (target.getBeneficiaryStudentId() == null) target.setBeneficiaryStudentId(source.getBeneficiaryStudentId());
        if (target.getProductId() == null) target.setProductId(source.getProductId());
        if (target.getPriceId() == null) target.setPriceId(source.getPriceId());
        if (target.getCurrency() == null) target.setCurrency(source.getCurrency());
    }

    private PaymentSubscription selectExistingWithRetry(String stripeSubscriptionId) {
        for (int i = 0; i < 6; i++) {
            PaymentSubscription existing = subscriptionMapper.selectByStripeId(stripeSubscriptionId);
            if (existing != null) return existing;
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    private void applyRemote(PaymentSubscription local, Subscription remote, Event event) {
        local.setStatus(remote.getStatus());
        Long currentPeriodStart = firstNonNull(remote.getCurrentPeriodStart(), extractItemPeriod(event, "current_period_start"));
        Long currentPeriodEnd = firstNonNull(remote.getCurrentPeriodEnd(), extractItemPeriod(event, "current_period_end"));
        if (currentPeriodStart == null) {
            currentPeriodStart = firstNonNull(remote.getStartDate(), remote.getBillingCycleAnchor());
        }
        if (currentPeriodEnd == null && currentPeriodStart != null) {
            currentPeriodEnd = estimatePeriodEnd(remote, currentPeriodStart);
        }
        if (currentPeriodStart == null || currentPeriodEnd == null) {
            log.warn("Subscription {} missing period fields, start={}, end={}",
                    remote.getId(), currentPeriodStart, currentPeriodEnd);
        }
        if (currentPeriodStart != null) local.setCurrentPeriodStart(new Date(currentPeriodStart * 1000L));
        if (currentPeriodEnd != null) local.setCurrentPeriodEnd(new Date(currentPeriodEnd * 1000L));
        local.setCancelAtPeriodEnd(Boolean.TRUE.equals(remote.getCancelAtPeriodEnd()) ? 1 : 0);
        if (remote.getCanceledAt() != null) local.setCanceledAt(new Date(remote.getCanceledAt() * 1000L));
        if (remote.getEndedAt() != null) local.setEndedAt(new Date(remote.getEndedAt() * 1000L));
        local.setLatestInvoiceId(remote.getLatestInvoice());
    }

    private static String extractStripePriceId(Subscription sub) {
        if (sub.getItems() == null || sub.getItems().getData() == null || sub.getItems().getData().isEmpty()) return null;
        return sub.getItems().getData().get(0).getPrice().getId();
    }

    private static Long extractItemPeriod(Event event, String fieldName) {
        if (event == null) return null;
        try {
            JsonObject root = JsonParser.parseString(event.getDataObjectDeserializer().getRawJson()).getAsJsonObject();
            if (!root.has("items") || root.get("items").isJsonNull()) return null;
            JsonObject items = root.getAsJsonObject("items");
            if (!items.has("data") || !items.get("data").isJsonArray() || items.getAsJsonArray("data").size() == 0) {
                return null;
            }
            JsonObject item = items.getAsJsonArray("data").get(0).getAsJsonObject();
            if (!item.has(fieldName) || item.get(fieldName).isJsonNull()) return null;
            return item.get(fieldName).getAsLong();
        } catch (Exception e) {
            log.warn("Failed to extract subscription item period field {} from event {}: {}",
                    fieldName, event.getId(), e.getMessage());
            return null;
        }
    }

    private static Long estimatePeriodEnd(Subscription remote, Long currentPeriodStart) {
        if (remote.getItems() == null || remote.getItems().getData() == null || remote.getItems().getData().isEmpty()) {
            return null;
        }
        SubscriptionItem item = remote.getItems().getData().get(0);
        Price price = item.getPrice();
        if (price == null || price.getRecurring() == null || price.getRecurring().getInterval() == null) {
            return null;
        }
        Long intervalCount = price.getRecurring().getIntervalCount();
        int count = intervalCount == null ? 1 : intervalCount.intValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentPeriodStart * 1000L);
        String interval = price.getRecurring().getInterval();
        if ("day".equals(interval)) {
            calendar.add(Calendar.DAY_OF_MONTH, count);
        } else if ("week".equals(interval)) {
            calendar.add(Calendar.WEEK_OF_YEAR, count);
        } else if ("month".equals(interval)) {
            calendar.add(Calendar.MONTH, count);
        } else if ("year".equals(interval)) {
            calendar.add(Calendar.YEAR, count);
        } else {
            return null;
        }
        return calendar.getTimeInMillis() / 1000L;
    }

    private static Long firstNonNull(Long first, Long second) {
        return first != null ? first : second;
    }

    private static Integer parseInt(Map<String, String> meta, String key) {
        if (meta == null) return null;
        String v = meta.get(key);
        if (v == null) return null;
        try { return Integer.valueOf(v); } catch (NumberFormatException e) { return null; }
    }
}
