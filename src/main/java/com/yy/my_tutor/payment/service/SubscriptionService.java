package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.SubscriptionDTO;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionDTO> listMy(Integer payerUserId, String status, Integer beneficiaryStudentId);
    SubscriptionDTO getDetail(Integer subId, Integer payerUserId);
    SubscriptionDTO cancelAtPeriodEnd(Integer subId, Integer payerUserId);
    SubscriptionDTO resume(Integer subId, Integer payerUserId);
    SubscriptionDTO applyPaymentMethod(Integer subId, Integer payerUserId, String paymentMethodId);
    SubscriptionDTO cancelNow(Integer subId, Integer operatorUserId);
}
