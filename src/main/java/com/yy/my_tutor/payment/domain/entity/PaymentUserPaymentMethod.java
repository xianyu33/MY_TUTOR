package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_method")
public class PaymentUserPaymentMethod {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String stripeCustomerId;
    private String stripePaymentMethodId;
    private String type;
    private String brand;
    private String last4;
    private Integer expMonth;
    private Integer expYear;
    private String country;
    private String funding;
    private String status;
    private Integer isDefault;
    private String setupIntentId;
    private Date createAt;
    private Date updateAt;
    private String deleteFlag;
}
