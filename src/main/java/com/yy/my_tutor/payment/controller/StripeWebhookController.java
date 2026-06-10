package com.yy.my_tutor.payment.controller;

import com.yy.my_tutor.payment.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class StripeWebhookController {

    @Resource private WebhookService webhookService;

    /**
     * 必须用 String 接 raw body — 签名校验依赖字节级一致,不能让 Spring 反序列化。
     * consumes = ALL_VALUE 防止部分代理改 Content-Type 导致请求接不到。
     */
    @PostMapping(value = "/api/payment/stripe/webhook", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> handle(@RequestBody String rawBody,
                                         @RequestHeader(value = "Stripe-Signature", required = false) String sigHeader) {
        if (sigHeader == null || sigHeader.isEmpty()) {
            log.warn("Stripe webhook called without Stripe-Signature header");
            return ResponseEntity.status(400).body("missing signature");
        }
        boolean ok = webhookService.process(rawBody, sigHeader);
        if (!ok) {
            return ResponseEntity.status(500).body("handler failed");
        }
        return ResponseEntity.ok("ok");
    }
}
