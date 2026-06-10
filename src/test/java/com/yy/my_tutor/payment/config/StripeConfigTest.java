package com.yy.my_tutor.payment.config;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class StripeConfigTest {

    @Test
    void init_should_fail_when_api_key_blank() {
        StripeConfig cfg = new StripeConfig();
        cfg.setMode("test");
        cfg.setWebhookSecret("whsec_xxx");
        assertThatThrownBy(cfg::init)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("api-key 未配置");
    }

    @Test
    void init_should_fail_when_webhook_secret_blank() {
        StripeConfig cfg = new StripeConfig();
        cfg.setMode("test");
        cfg.setApiKey("sk_test_xxx");
        assertThatThrownBy(cfg::init)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("webhook-secret 未配置");
    }

    @Test
    void init_should_fail_when_live_mode_uses_test_key() {
        StripeConfig cfg = new StripeConfig();
        cfg.setMode("live");
        cfg.setApiKey("sk_test_xxx");
        cfg.setWebhookSecret("whsec_xxx");
        assertThatThrownBy(cfg::init)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sk_live_");
    }

    @Test
    void init_should_pass_with_valid_test_config() {
        StripeConfig cfg = new StripeConfig();
        cfg.setMode("test");
        cfg.setApiKey("sk_test_abc");
        cfg.setWebhookSecret("whsec_abc");
        cfg.setApiVersion("2024-06-20");
        cfg.init();
        assertThat(com.stripe.Stripe.apiKey).isEqualTo("sk_test_abc");
    }

    @Test
    void isAdmin_returns_true_for_configured_user_id() {
        StripeConfig cfg = new StripeConfig();
        cfg.setAdminUserIds(Arrays.asList(1, 2, 3));
        assertThat(cfg.isAdmin(2)).isTrue();
        assertThat(cfg.isAdmin(99)).isFalse();
        assertThat(cfg.isAdmin(null)).isFalse();
    }
}
