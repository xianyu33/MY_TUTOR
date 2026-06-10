package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class CreateCheckoutRequest {
    private Integer priceId;
    private Integer beneficiaryStudentId;
    private String successUrl;
    private String cancelUrl;
}
