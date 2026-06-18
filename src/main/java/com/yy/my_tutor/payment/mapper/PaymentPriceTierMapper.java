package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentPriceTier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentPriceTierMapper extends BaseMapper<PaymentPriceTier> {

    @Select("SELECT * FROM payment_price_tier WHERE price_id = #{priceId} " +
            "AND min_quantity <= #{quantity} " +
            "AND (max_quantity IS NULL OR max_quantity >= #{quantity}) " +
            "AND status = 1 AND delete_flag = 'N' LIMIT 1")
    PaymentPriceTier selectActiveTier(@Param("priceId") Integer priceId,
                                      @Param("quantity") Integer quantity);
}
