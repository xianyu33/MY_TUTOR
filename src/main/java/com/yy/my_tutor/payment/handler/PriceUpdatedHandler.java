package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Price;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PriceUpdatedHandler implements EventHandler {

    @Resource private PaymentPriceMapper priceMapper;

    @Override public String eventType() { return "price.updated"; }

    @Override
    public HandlerResult handle(Event event) {
        Price remote = EventObjectExtractor.get(event, Price.class);
        if (remote == null) return HandlerResult.SKIPPED;
        PaymentPrice local = priceMapper.selectByStripeId(remote.getId());
        if (local == null) return HandlerResult.SKIPPED;
        Integer newStatus = Boolean.TRUE.equals(remote.getActive()) ? 1 : 0;
        if (!newStatus.equals(local.getStatus())) {
            local.setStatus(newStatus);
            local.setUpdateBy("STRIPE_WEBHOOK");
            priceMapper.updateById(local);
            log.info("Price {} status → {} via webhook", remote.getId(), newStatus);
        }
        return HandlerResult.SUCCESS;
    }
}
