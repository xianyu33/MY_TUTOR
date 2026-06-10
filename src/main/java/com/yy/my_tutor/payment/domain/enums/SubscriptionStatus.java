package com.yy.my_tutor.payment.domain.enums;

/** 完全镜像 Stripe Subscription.status,字段值与 Stripe API 字符串一致(小写,蛇形) */
public enum SubscriptionStatus {
    incomplete,
    incomplete_expired,
    trialing,
    active,
    past_due,
    canceled,
    unpaid,
    paused;

    public static SubscriptionStatus fromStripe(String s) {
        if (s == null) return null;
        for (SubscriptionStatus v : values()) {
            if (v.name().equals(s)) return v;
        }
        throw new IllegalArgumentException("未识别的 Stripe subscription status: " + s);
    }

    public boolean isEntitled() {
        return this == active || this == trialing;
    }
}
