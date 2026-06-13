package com.yy.my_tutor.payment.handler;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
final class EventObjectExtractor {

    private EventObjectExtractor() {
    }

    static <T extends StripeObject> T get(Event event, Class<T> type) {
        Optional<StripeObject> object = event.getDataObjectDeserializer().getObject();
        StripeObject stripeObject = object.orElse(null);
        if (stripeObject == null) {
            try {
                stripeObject = event.getDataObjectDeserializer().deserializeUnsafe();
            } catch (EventDataObjectDeserializationException e) {
                log.warn("Stripe event {} data object deserialize failed, apiVersion={}, type={}: {}",
                        event.getId(), event.getApiVersion(), event.getType(), e.getMessage());
                return null;
            }
        }
        if (!type.isInstance(stripeObject)) {
            log.warn("Stripe event {} expected {}, got {}",
                    event.getId(), type.getSimpleName(), stripeObject.getClass().getSimpleName());
            return null;
        }
        return type.cast(stripeObject);
    }
}
