package com.yy.my_tutor.payment.handler;

import com.stripe.model.Event;

public interface EventHandler {
    /** 该 handler 处理的 Stripe event.type,例如 "checkout.session.completed" */
    String eventType();

    /** 处理事件,返回 SUCCESS/SKIPPED/FAILED。抛异常视为 FAILED */
    HandlerResult handle(Event event);
}
