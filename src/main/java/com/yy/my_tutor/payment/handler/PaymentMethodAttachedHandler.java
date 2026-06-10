package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentMethodAttachedHandler implements EventHandler {
    @Override public String eventType() { return "payment_method.attached"; }
    @Override public HandlerResult handle(Event event) {
        PaymentMethod pm = (PaymentMethod) event.getDataObjectDeserializer().getObject().orElse(null);
        if (pm != null) log.info("payment_method.attached: id={} customer={}", pm.getId(), pm.getCustomer());
        return HandlerResult.SUCCESS;
    }
}
