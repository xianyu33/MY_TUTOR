package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentProductMapper extends BaseMapper<PaymentProduct> {

    @Select("SELECT * FROM payment_product WHERE stripe_product_id = #{stripeProductId} AND delete_flag = 'N' LIMIT 1")
    PaymentProduct selectByStripeId(@Param("stripeProductId") String stripeProductId);
}
