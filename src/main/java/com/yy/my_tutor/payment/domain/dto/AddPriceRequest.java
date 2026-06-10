package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class AddPriceRequest {
    private String currency;
    private Long unitAmount;
    private String billingInterval;      // null=一次性
}
