package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InvoiceCreatedHandler implements EventHandler {
    @Override public String eventType() { return "invoice.created"; }
    @Override public HandlerResult handle(Event event) {
        Invoice inv = EventObjectExtractor.get(event, Invoice.class);
        if (inv != null) log.info("invoice.created received: id={} subscription={}", inv.getId(), inv.getSubscription());
        return HandlerResult.SUCCESS;
    }
}
