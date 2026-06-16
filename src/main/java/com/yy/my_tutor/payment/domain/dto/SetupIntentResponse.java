package com.yy.my_tutor.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetupIntentResponse {
    private String setupIntentId;
    private String clientSecret;
}
