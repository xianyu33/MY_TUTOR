package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class PriceDefDTO {
    private String currency;             // usd/eur/cny
    private Long unitAmount;             // 最小货币单位
    private String billingInterval;      // null=一次性, month/quarter/year
}
