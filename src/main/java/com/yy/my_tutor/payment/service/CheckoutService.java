package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.user.domain.User;

public interface CheckoutService {
    CheckoutSessionResponse createSession(CreateCheckoutRequest req, Integer payerUserId);
    CheckoutSessionResponse createSession(CreateCheckoutRequest req, User payer);

    /** 创建 setup 模式 session 用于换卡 */
    CheckoutSessionResponse createSetupSession(Integer payerUserId, String returnUrl);
    CheckoutSessionResponse createSetupSession(User payer, String returnUrl);

    PaymentOrder getOrderStatus(String orderNo, Integer payerUserId);
    PaymentOrder getOrderStatus(String orderNo, User payer);
}
