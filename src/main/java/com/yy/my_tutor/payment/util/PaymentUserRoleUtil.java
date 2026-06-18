package com.yy.my_tutor.payment.util;

import com.yy.my_tutor.user.domain.User;

public final class PaymentUserRoleUtil {

    private PaymentUserRoleUtil() {
    }

    public static String roleOf(User user) {
        if (user == null) {
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "请先登录");
        }
        if ("S".equals(user.getRole())) {
            return "S";
        }
        if (user.getType() != null && user.getType() == 1) {
            return "T";
        }
        return "P";
    }
}
