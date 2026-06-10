package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.CheckoutSessionResponse;
import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;

public interface CheckoutService {
    CheckoutSessionResponse createSession(CreateCheckoutRequest req, Integer payerUserId);

    /** 创建 setup 模式 session 用于换卡 */
    CheckoutSessionResponse createSetupSession(Integer payerUserId, String returnUrl);

    PaymentOrder getOrderStatus(String orderNo, Integer payerUserId);
}
