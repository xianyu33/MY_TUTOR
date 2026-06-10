package com.yy.my_tutor.payment.domain.dto;

import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import lombok.Data;

import java.util.Date;

@Data
public class OrderListItemDTO {
    private Integer id;
    private String orderNo;
    private Integer beneficiaryStudentId;
    private Integer productId;
    private String productName;
    private String currency;
    private Long amount;
    private String status;
    private Integer subscriptionId;
    private Date paidAt;
    private Date createAt;

    public static OrderListItemDTO from(PaymentOrder o, String productName) {
        OrderListItemDTO d = new OrderListItemDTO();
        d.setId(o.getId());
        d.setOrderNo(o.getOrderNo());
        d.setBeneficiaryStudentId(o.getBeneficiaryStudentId());
        d.setProductId(o.getProductId());
        d.setProductName(productName);
        d.setCurrency(o.getCurrency());
        d.setAmount(o.getAmount());
        d.setStatus(o.getStatus());
        d.setSubscriptionId(o.getSubscriptionId());
        d.setPaidAt(o.getPaidAt());
        d.setCreateAt(o.getCreateAt());
        return d;
    }
}
