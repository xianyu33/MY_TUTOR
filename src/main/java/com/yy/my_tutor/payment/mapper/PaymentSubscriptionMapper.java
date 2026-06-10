package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentSubscriptionMapper extends BaseMapper<PaymentSubscription> {

    @Select("SELECT * FROM payment_subscription WHERE stripe_subscription_id = #{stripeSubId} AND delete_flag = 'N' LIMIT 1")
    PaymentSubscription selectByStripeId(@Param("stripeSubId") String stripeSubId);

    @Select("SELECT COUNT(*) FROM payment_subscription WHERE beneficiary_student_id = #{studentId} " +
            "AND status IN ('active','trialing') AND current_period_end > NOW() AND delete_flag = 'N'")
    int countActiveByStudent(@Param("studentId") Integer studentId);
}
