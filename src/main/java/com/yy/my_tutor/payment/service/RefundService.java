package com.yy.my_tutor.payment.service;

import com.yy.my_tutor.payment.domain.dto.RefundRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;

public interface RefundService {
    PaymentRefund refundOrder(String orderNo, RefundRequest req, Integer operatorUserId);
}
