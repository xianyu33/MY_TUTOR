package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.entity.StripeEvent;
import com.yy.my_tutor.payment.handler.EventHandler;
import com.yy.my_tutor.payment.handler.HandlerResult;
import com.yy.my_tutor.payment.mapper.StripeEventMapper;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WebhookServiceImpl implements WebhookService {

    @Resource private StripeClientService stripeClient;
    @Resource private StripeConfig stripeConfig;
    @Resource private StripeEventMapper eventMapper;
    @Resource private List<EventHandler> handlers;

    private Map<String, EventHandler> handlerMap;

    @PostConstruct
    public void initHandlerMap() {
        handlerMap = new HashMap<>();
        for (EventHandler h : handlers) {
            EventHandler existing = handlerMap.put(h.eventType(), h);
            if (existing != null) {
                throw new IllegalStateException("Duplicate EventHandler for " + h.eventType() +
                        ": " + existing.getClass() + " vs " + h.getClass());
            }
        }
        log.info("Registered {} Stripe event handlers: {}", handlerMap.size(), handlerMap.keySet());
    }

    @Override
    public boolean process(String rawBody, String signatureHeader) {
        // 1. 签名校验
        Event event;
        try {
            event = stripeClient.constructWebhookEvent(rawBody, signatureHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.warn("Stripe signature invalid: {}", e.getMessage());
            return false;
        }

        // 2. INSERT IGNORE 幂等
        StripeEvent record = new StripeEvent();
        record.setStripeEventId(event.getId());
        record.setEventType(event.getType());
        record.setApiVersion(event.getApiVersion());
        record.setPayload(rawBody);
        int affected = eventMapper.insertIgnore(record);
        StripeEvent saved = eventMapper.selectByStripeEventId(event.getId());
        if (affected == 0) {
            if (saved == null) {
                log.warn("Stripe event {} already exists but cannot be loaded, skip", event.getId());
                return true;
            }
            if (saved.getProcessStatus() == null || saved.getProcessStatus() != 2) {
                log.info("Stripe event {} already handled with status={}, skip", event.getId(), saved.getProcessStatus());
                return true;
            }
            log.warn("Retry failed Stripe event {} type={}, retryCount={}",
                    event.getId(), event.getType(), saved.getRetryCount());
            saved.setProcessStatus(0);
            saved.setLastError(null);
            saved.setPayload(rawBody);
            saved.setRetryCount((saved.getRetryCount() == null ? 0 : saved.getRetryCount()) + 1);
            eventMapper.updateById(saved);
        }

        // 3. dispatch
        EventHandler handler = handlerMap.get(event.getType());
        if (handler == null) {
            log.info("No handler registered for {}, mark SKIPPED", event.getType());
            saved.setProcessStatus(3);
            saved.setLastError(null);
            saved.setProcessedAt(new Date());
            eventMapper.updateById(saved);
            return true;
        }

        try {
            HandlerResult result = handler.handle(event);
            saved.setProcessedAt(new Date());
            switch (result) {
                case SUCCESS: saved.setProcessStatus(1);
                              saved.setLastError(null); break;
                case SKIPPED: saved.setProcessStatus(3);
                              saved.setLastError(null); break;
                case FAILED:  saved.setProcessStatus(2);
                              saved.setRetryCount((saved.getRetryCount() == null ? 0 : saved.getRetryCount()) + 1);
                              saved.setLastError("handler returned FAILED"); break;
            }
            eventMapper.updateById(saved);
            return result != HandlerResult.FAILED;
        } catch (Exception e) {
            log.error("Handler {} threw for event {}", handler.getClass().getSimpleName(), event.getId(), e);
            saved.setProcessStatus(2);
            saved.setLastError(truncate(e.getClass().getSimpleName() + ": " + e.getMessage(), 1000));
            saved.setProcessedAt(new Date());
            saved.setRetryCount((saved.getRetryCount() == null ? 0 : saved.getRetryCount()) + 1);
            eventMapper.updateById(saved);
            return false;
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
