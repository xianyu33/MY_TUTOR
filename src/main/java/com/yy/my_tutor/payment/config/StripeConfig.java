package com.yy.my_tutor.payment.config;

import com.stripe.Stripe;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {

    private String mode;
    private String apiKey;
    private String webhookSecret;
    private String apiVersion;
    private String defaultCurrency;
    private List<String> supportedCurrencies;
    private Checkout checkout = new Checkout();
    private Refund refund = new Refund();
    private EntitlementCache entitlementCache = new EntitlementCache();
    private List<Integer> adminUserIds;

    @PostConstruct
    public void init() {
        Assert.hasText(apiKey, "stripe.api-key 未配置,拒绝启动 (请设置环境变量 STRIPE_API_KEY)");
        Assert.hasText(webhookSecret, "stripe.webhook-secret 未配置,拒绝启动 (请设置环境变量 STRIPE_WEBHOOK_SECRET)");
        if ("live".equals(mode)) {
            Assert.isTrue(apiKey.startsWith("sk_live_"), "live 模式必须使用 sk_live_ 开头的密钥");
            Assert.isTrue(webhookSecret.startsWith("whsec_"), "webhook secret 格式错误");
        }
        Stripe.apiKey = apiKey;
        Stripe.setAppInfo("MY_TUTOR", "1.0.0", "https://mytutor.top");
        log.info("Stripe SDK initialized in {} mode, api-version={}", mode, apiVersion);
    }

    public boolean isAdmin(Integer userId) {
        return userId != null && adminUserIds != null && adminUserIds.contains(userId);
    }

    public Set<String> supportedCurrenciesSet() {
        return supportedCurrencies == null ? Collections.emptySet() : new HashSet<>(supportedCurrencies);
    }

    @Data
    public static class Checkout {
        private Integer sessionExpiresAfterHours = 24;
        private String successUrlTemplate;
        private String cancelUrlTemplate;
        private List<String> allowedReturnHosts;
    }

    @Data
    public static class Refund {
        private Integer windowDays = 180;
    }

    @Data
    public static class EntitlementCache {
        private Integer ttlSeconds = 60;
    }
}
