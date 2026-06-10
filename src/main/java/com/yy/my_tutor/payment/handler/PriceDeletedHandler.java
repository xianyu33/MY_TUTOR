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
public class PriceDeletedHandler implements EventHandler {

    @Resource private PaymentPriceMapper priceMapper;

    @Override public String eventType() { return "price.deleted"; }

    @Override
    public HandlerResult handle(Event event) {
        Price remote = (Price) event.getDataObjectDeserializer().getObject().orElse(null);
        if (remote == null) return HandlerResult.SKIPPED;
        PaymentPrice local = priceMapper.selectByStripeId(remote.getId());
        if (local == null) return HandlerResult.SKIPPED;
        local.setStatus(0);
        local.setUpdateBy("STRIPE_WEBHOOK");
        priceMapper.updateById(local);
        log.info("Price {} status → 0 via webhook (deleted)", remote.getId());
        return HandlerResult.SUCCESS;
    }
}
