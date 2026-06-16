package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentUserPaymentMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PaymentMethodMapper extends BaseMapper<PaymentUserPaymentMethod> {

    @Select("SELECT * FROM payment_method WHERE stripe_payment_method_id = #{paymentMethodId} AND delete_flag = 'N' LIMIT 1")
    PaymentUserPaymentMethod selectByStripePaymentMethodId(@Param("paymentMethodId") String paymentMethodId);

    @Select("SELECT COUNT(*) FROM payment_method WHERE user_id = #{userId} AND status = 'ACTIVE' AND delete_flag = 'N'")
    int countActiveByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM payment_method WHERE user_id = #{userId} AND is_default = 1 AND status = 'ACTIVE' AND delete_flag = 'N' LIMIT 1")
    PaymentUserPaymentMethod selectDefaultByUserId(@Param("userId") Integer userId);

    @Update("UPDATE payment_method SET is_default = 0 WHERE user_id = #{userId} AND delete_flag = 'N'")
    int clearDefaultForUser(@Param("userId") Integer userId);

    @Update("UPDATE payment_method SET status = #{status}, is_default = 0 WHERE stripe_payment_method_id = #{paymentMethodId} AND delete_flag = 'N'")
    int updateStatusByStripePaymentMethodId(@Param("paymentMethodId") String paymentMethodId,
                                            @Param("status") String status);
}
