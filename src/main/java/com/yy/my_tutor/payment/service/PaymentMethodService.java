package com.yy.my_tutor.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.yy.my_tutor.payment.domain.dto.PaymentMethodDTO;
import com.yy.my_tutor.payment.domain.dto.SetupIntentResponse;
import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import com.yy.my_tutor.user.domain.User;

import java.util.List;

public interface PaymentMethodService {
    SetupIntentResponse createSetupIntent(Integer payerUserId);
    SetupIntentResponse createSetupIntent(User payer);
    PaymentUserPaymentMethod syncAttachedPaymentMethod(PaymentMethod paymentMethod, String setupIntentId) throws StripeException;
    List<PaymentMethodDTO> listMy(Integer payerUserId);
    List<PaymentMethodDTO> listMy(User payer);
    PaymentMethodDTO setDefault(Integer payerUserId, Integer methodId);
    PaymentMethodDTO setDefault(User payer, Integer methodId);
    void detach(Integer payerUserId, Integer methodId);
    void detach(User payer, Integer methodId);
    PaymentUserPaymentMethod defaultMethod(Integer payerUserId);
    PaymentUserPaymentMethod defaultMethod(User payer);
}
