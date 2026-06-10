package com.yy.my_tutor.payment.domain.dto;

import lombok.Data;

@Data
public class RefundRequest {
    private Long amount;            // null = 全额
    private String reason;          // duplicate/fraudulent/requested_by_customer
    private String reasonDetail;
}
