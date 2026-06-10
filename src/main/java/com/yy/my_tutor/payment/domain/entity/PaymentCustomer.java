package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_customer")
public class PaymentCustomer {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String stripeCustomerId;
    private String email;
    private String defaultPaymentMethod;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
