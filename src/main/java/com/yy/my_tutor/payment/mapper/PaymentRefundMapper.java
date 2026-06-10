package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentRefundMapper extends BaseMapper<PaymentRefund> {

    @Select("SELECT * FROM payment_refund WHERE stripe_refund_id = #{stripeRefundId} AND delete_flag = 'N' LIMIT 1")
    PaymentRefund selectByStripeId(@Param("stripeRefundId") String stripeRefundId);
}
