package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentCustomerMapper extends BaseMapper<PaymentCustomer> {

    @Select("SELECT * FROM payment_customer WHERE user_id = #{userId} AND delete_flag = 'N' LIMIT 1")
    PaymentCustomer selectByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM payment_customer WHERE user_id = #{userId} AND user_role = #{userRole} AND delete_flag = 'N' LIMIT 1")
    PaymentCustomer selectByUserIdAndRole(@Param("userId") Integer userId,
                                          @Param("userRole") String userRole);

    @Select("SELECT * FROM payment_customer WHERE stripe_customer_id = #{stripeCustomerId} AND delete_flag = 'N' LIMIT 1")
    PaymentCustomer selectByStripeId(@Param("stripeCustomerId") String stripeCustomerId);
}
