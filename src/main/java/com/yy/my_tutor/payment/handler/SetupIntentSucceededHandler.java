package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.yy.my_tutor.payment.service.PaymentMethodService;
import com.yy.my_tutor.payment.service.StripeClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SetupIntentSucceededHandler implements EventHandler {
    @Resource private StripeClientService stripeClient;
    @Resource private PaymentMethodService paymentMethodService;

    @Override public String eventType() { return "setup_intent.succeeded"; }

    @Override
    public HandlerResult handle(Event event) {
        SetupIntent setupIntent = EventObjectExtractor.get(event, SetupIntent.class);
        if (setupIntent == null || setupIntent.getPaymentMethod() == null) return HandlerResult.FAILED;
        try {
            PaymentMethod pm = setupIntent.getPaymentMethodObject() != null
                    ? setupIntent.getPaymentMethodObject()
                    : stripeClient.retrievePaymentMethod(setupIntent.getPaymentMethod());
            paymentMethodService.syncAttachedPaymentMethod(pm, setupIntent.getId());
            log.info("setup_intent.succeeded synced payment method {} for customer {}",
                    pm.getId(), pm.getCustomer());
            return HandlerResult.SUCCESS;
        } catch (Exception e) {
            log.error("setup_intent.succeeded sync failed: setupIntent={}", setupIntent.getId(), e);
            return HandlerResult.FAILED;
        }
    }
}
