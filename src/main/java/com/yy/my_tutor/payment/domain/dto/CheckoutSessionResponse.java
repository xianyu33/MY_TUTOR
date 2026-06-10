package com.yy.my_tutor.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSessionResponse {
    private String sessionId;
    private String url;
    private String orderNo;
}
