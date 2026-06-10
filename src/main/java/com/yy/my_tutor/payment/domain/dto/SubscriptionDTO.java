package com.yy.my_tutor.payment.domain.dto;

import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import lombok.Data;

@Data
public class SubscriptionDTO {
    private PaymentSubscription subscription;
    private String productName;
    private Long unitAmount;
    private String billingInterval;

    public static SubscriptionDTO of(PaymentSubscription s, String productName, Long unitAmount, String billingInterval) {
        SubscriptionDTO d = new SubscriptionDTO();
        d.setSubscription(s);
        d.setProductName(productName);
        d.setUnitAmount(unitAmount);
        d.setBillingInterval(billingInterval);
        return d;
    }
}
