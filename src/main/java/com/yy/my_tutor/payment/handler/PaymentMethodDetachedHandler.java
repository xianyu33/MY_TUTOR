package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.yy.my_tutor.payment.mapper.PaymentMethodMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PaymentMethodDetachedHandler implements EventHandler {
    @Resource private PaymentMethodMapper paymentMethodMapper;

    @Override public String eventType() { return "payment_method.detached"; }

    @Override
    public HandlerResult handle(Event event) {
        PaymentMethod pm = EventObjectExtractor.get(event, PaymentMethod.class);
        if (pm == null || pm.getId() == null) return HandlerResult.FAILED;
        paymentMethodMapper.updateStatusByStripePaymentMethodId(pm.getId(), "DETACHED");
        log.info("payment_method.detached marked local method detached: id={}", pm.getId());
        return HandlerResult.SUCCESS;
    }
}
