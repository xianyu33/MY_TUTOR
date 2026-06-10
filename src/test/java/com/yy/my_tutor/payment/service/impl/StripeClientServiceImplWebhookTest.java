package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StripeClientServiceImplWebhookTest {

    private static final String SECRET = "whsec_test_secret_value";
    private static final String PAYLOAD = "{\"id\":\"evt_test_123\",\"object\":\"event\",\"type\":\"checkout.session.completed\"}";

    private final StripeClientServiceImpl client = new StripeClientServiceImpl();

    @Test
    void constructWebhookEvent_valid_signature_returns_event() throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String signedPayload = timestamp + "." + PAYLOAD;
        String signature = hmacSha256(SECRET, signedPayload);
        String sigHeader = "t=" + timestamp + ",v1=" + signature;

        Event event = client.constructWebhookEvent(PAYLOAD, sigHeader, SECRET);

        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo("evt_test_123");
        assertThat(event.getType()).isEqualTo("checkout.session.completed");
    }

    @Test
    void constructWebhookEvent_invalid_signature_throws() {
        String badSigHeader = "t=1234567890,v1=deadbeef";
        assertThatThrownBy(() -> client.constructWebhookEvent(PAYLOAD, badSigHeader, SECRET))
                .isInstanceOf(SignatureVerificationException.class);
    }

    private static String hmacSha256(String secret, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) hex.append(String.format("%02x", b));
        return hex.toString();
    }
}
