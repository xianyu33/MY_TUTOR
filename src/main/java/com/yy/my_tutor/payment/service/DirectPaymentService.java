package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.CreateCheckoutRequest;
import com.yy.my_tutor.payment.domain.dto.DirectPaymentResponse;
import com.yy.my_tutor.user.domain.User;

public interface DirectPaymentService {
    DirectPaymentResponse pay(CreateCheckoutRequest req, Integer payerUserId);
    DirectPaymentResponse pay(CreateCheckoutRequest req, User payer);
    DirectPaymentResponse createSubscription(CreateCheckoutRequest req, Integer payerUserId);
    DirectPaymentResponse createSubscription(CreateCheckoutRequest req, User payer);
}
