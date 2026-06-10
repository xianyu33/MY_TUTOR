package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentPriceMapper extends BaseMapper<PaymentPrice> {

    @Select("SELECT * FROM payment_price WHERE stripe_price_id = #{stripePriceId} AND delete_flag = 'N' LIMIT 1")
    PaymentPrice selectByStripeId(@Param("stripePriceId") String stripePriceId);

    @Select("SELECT * FROM payment_price WHERE product_id = #{productId} AND delete_flag = 'N' AND status = 1")
    List<PaymentPrice> selectActiveByProductId(@Param("productId") Integer productId);
}
