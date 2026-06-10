package com.yy.my_tutor.payment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("stripe_event")
public class StripeEvent {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String stripeEventId;
    private String eventType;
    private String apiVersion;
    private String payload;
    private Integer processStatus;
    private Integer retryCount;
    private String lastError;
    private Date receivedAt;
    private Date processedAt;
}
