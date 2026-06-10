package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_product")
public class PaymentProduct {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String stripeProductId;
    private String productType;
    private Integer targetRefId;
    private String name;
    private String nameZh;
    private String nameFr;
    private String description;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
