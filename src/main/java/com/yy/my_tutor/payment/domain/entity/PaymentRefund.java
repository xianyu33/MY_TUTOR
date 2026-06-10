package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_refund")
public class PaymentRefund {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private String stripeRefundId;
    private Long amount;
    private String currency;
    private String reason;
    private String reasonDetail;
    private String status;
    private Integer operatorUserId;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
