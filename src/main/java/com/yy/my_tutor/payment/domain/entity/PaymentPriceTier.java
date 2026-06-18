package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("payment_price_tier")
public class PaymentPriceTier {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer priceId;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Long unitAmount;
    private Integer status;
    private Date createAt;
    private String createBy;
    private Date updateAt;
    private String updateBy;
    private String deleteFlag;
}
