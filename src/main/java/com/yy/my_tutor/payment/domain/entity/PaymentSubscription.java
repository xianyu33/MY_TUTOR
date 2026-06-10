package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_subscription")
public class PaymentSubscription {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String stripeSubscriptionId;
    private Integer payerUserId;
    private Integer beneficiaryStudentId;
    private Integer productId;
    private Integer priceId;
    private String currency;
    private String status;
    private Date currentPeriodStart;
    private Date currentPeriodEnd;
    private Integer cancelAtPeriodEnd;
    private Date canceledAt;
    private Date endedAt;
    private String latestInvoiceId;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
