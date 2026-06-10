package com.yy.my_tutor.payment.domain.dto;

import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailDTO {
    private PaymentOrder order;
    private String productName;
    private List<PaymentRefund> refunds;
}
