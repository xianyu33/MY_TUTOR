package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_order")
public class PaymentOrder {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String orderNo;
    private Integer payerUserId;
    private Integer beneficiaryStudentId;
    private Integer productId;
    private Integer priceId;
    private String currency;
    private Long amount;
    private String status;
    private String stripeCheckoutSessionId;
    private String stripePaymentIntentId;
    private String stripePaymentMethodId;
    private String stripeInvoiceId;
    private Integer subscriptionId;
    private Date paidAt;
    private Date expireAt;
    private String failureReason;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
