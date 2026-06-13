package com.yy.my_tutor.payment.handler;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class CheckoutSessionCompletedHandler implements EventHandler {

    @Resource private PaymentOrderMapper orderMapper;
    @Resource private StripeClientService stripeClient;
    @Resource private SubscriptionUpsertHelper subscriptionUpsertHelper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "checkout.session.completed"; }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HandlerResult handle(Event event) {
        Session session = EventObjectExtractor.get(event, Session.class);
        if (session == null) {
            log.warn("checkout.session.completed payload deserialize failed: {}", event.getId());
            return HandlerResult.FAILED;
        }

        PaymentOrder order = orderMapper.selectByStripeSessionId(session.getId());
        if (order == null) {
            String orderNo = session.getMetadata() == null ? null : session.getMetadata().get("local_order_no");
            if (orderNo != null) order = orderMapper.selectByOrderNo(orderNo);
        }
        if (order == null) {
            log.error("checkout.session.completed: cannot find local order for session {}", session.getId());
            return HandlerResult.FAILED;
        }

        if (!OrderStatus.PENDING.name().equals(order.getStatus())) {
            log.info("Order {} status={} not PENDING, skip", order.getOrderNo(), order.getStatus());
            return HandlerResult.SKIPPED;
        }

        order.setStatus(OrderStatus.PAID.name());
        order.setPaidAt(new Date(event.getCreated() * 1000L));
        order.setStripePaymentIntentId(session.getPaymentIntent());

        if ("subscription".equals(session.getMode()) && session.getSubscription() != null) {
            order.setStripeInvoiceId(session.getInvoice());
            try {
                Subscription remoteSub = stripeClient.retrieveSubscription(session.getSubscription());
                PaymentSubscription sub = subscriptionUpsertHelper.upsert(
                        remoteSub, event.getCreated(), session.getMetadata());
                if (sub != null) order.setSubscriptionId(sub.getId());
            } catch (StripeException e) {
                log.error("Failed to retrieve subscription {}", session.getSubscription(), e);
                return HandlerResult.FAILED;
            }
        }

        orderMapper.updateById(order);
        cacheInvalidator.invalidate(order.getBeneficiaryStudentId());
        log.info("Order {} → PAID via checkout.session.completed", order.getOrderNo());
        return HandlerResult.SUCCESS;
    }
}
