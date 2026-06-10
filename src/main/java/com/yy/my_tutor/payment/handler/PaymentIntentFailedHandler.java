package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PaymentIntentFailedHandler implements EventHandler {

    @Resource private PaymentOrderMapper orderMapper;

    @Override public String eventType() { return "payment_intent.payment_failed"; }

    @Override
    public HandlerResult handle(Event event) {
        PaymentIntent pi = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
        if (pi == null) return HandlerResult.FAILED;
        PaymentOrder order = orderMapper.selectByPaymentIntentId(pi.getId());
        if (order == null) return HandlerResult.SKIPPED;
        if (!OrderStatus.PENDING.name().equals(order.getStatus())) return HandlerResult.SKIPPED;
        order.setStatus(OrderStatus.FAILED.name());
        if (pi.getLastPaymentError() != null) {
            String reason = pi.getLastPaymentError().getMessage();
            order.setFailureReason(reason == null ? "unknown" : (reason.length() > 255 ? reason.substring(0,255) : reason));
        }
        orderMapper.updateById(order);
        log.info("Order {} → FAILED: {}", order.getOrderNo(), order.getFailureReason());
        return HandlerResult.SUCCESS;
    }
}
