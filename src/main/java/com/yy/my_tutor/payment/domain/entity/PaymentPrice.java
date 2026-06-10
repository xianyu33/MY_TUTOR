package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_price")
public class PaymentPrice {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer productId;
    private String stripePriceId;
    private String currency;
    private Long unitAmount;
    private String billingInterval;
    private Integer intervalCount;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
