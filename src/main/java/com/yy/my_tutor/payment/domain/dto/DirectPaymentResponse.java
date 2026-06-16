package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class DirectPaymentResponse {
    private String orderNo;
    private String paymentIntentId;
    private Integer subscriptionId;
    private String status;
    private String clientSecret;
    private boolean requiresAction;

    public static DirectPaymentResponse of(String orderNo, String paymentIntentId,
                                           Integer subscriptionId, String status,
                                           String clientSecret, boolean requiresAction) {
        DirectPaymentResponse r = new DirectPaymentResponse();
        r.orderNo = orderNo;
        r.paymentIntentId = paymentIntentId;
        r.subscriptionId = subscriptionId;
        r.status = status;
        r.clientSecret = clientSecret;
        r.requiresAction = requiresAction;
        return r;
    }
}
