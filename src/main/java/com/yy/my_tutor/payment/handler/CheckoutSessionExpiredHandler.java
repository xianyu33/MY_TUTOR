package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class CheckoutSessionExpiredHandler implements EventHandler {

    @Resource private PaymentOrderMapper orderMapper;

    @Override public String eventType() { return "checkout.session.expired"; }

    @Override
    public HandlerResult handle(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) return HandlerResult.FAILED;
        PaymentOrder order = orderMapper.selectByStripeSessionId(session.getId());
        if (order == null) return HandlerResult.SKIPPED;
        if (!OrderStatus.PENDING.name().equals(order.getStatus())) return HandlerResult.SKIPPED;
        order.setStatus(OrderStatus.EXPIRED.name());
        orderMapper.updateById(order);
        log.info("Order {} → EXPIRED", order.getOrderNo());
        return HandlerResult.SUCCESS;
    }
}
