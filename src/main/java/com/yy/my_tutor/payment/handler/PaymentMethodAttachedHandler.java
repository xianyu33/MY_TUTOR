package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PaymentMethodAttachedHandler implements EventHandler {
    @Resource private PaymentMethodService paymentMethodService;

    @Override public String eventType() { return "payment_method.attached"; }
    @Override public HandlerResult handle(Event event) {
        PaymentMethod pm = EventObjectExtractor.get(event, PaymentMethod.class);
        if (pm == null) return HandlerResult.FAILED;
        try {
            paymentMethodService.syncAttachedPaymentMethod(pm, null);
            log.info("payment_method.attached synced: id={} customer={}", pm.getId(), pm.getCustomer());
            return HandlerResult.SUCCESS;
        } catch (Exception e) {
            log.error("payment_method.attached sync failed: id={}", pm.getId(), e);
            return HandlerResult.FAILED;
        }
    }
}
