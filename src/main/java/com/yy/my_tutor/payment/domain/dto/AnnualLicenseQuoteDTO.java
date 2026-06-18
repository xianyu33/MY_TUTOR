package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class AnnualLicenseQuoteDTO {
    private Integer productId;
    private Integer priceId;
    private Integer priceTierId;
    private String currency;
    private Integer quantity;
    private Long unitAmount;
    private Long totalAmount;
}
