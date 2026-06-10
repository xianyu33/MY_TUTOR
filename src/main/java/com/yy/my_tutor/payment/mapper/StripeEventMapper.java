package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.StripeEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StripeEventMapper extends BaseMapper<StripeEvent> {

    /**
     * 幂等插入:已存在则 affected_rows=0,新事件 affected_rows=1。
     */
    @Insert("INSERT IGNORE INTO stripe_event(stripe_event_id, event_type, api_version, payload, received_at) " +
            "VALUES(#{stripeEventId}, #{eventType}, #{apiVersion}, #{payload}, NOW())")
    int insertIgnore(StripeEvent event);

    @Select("SELECT * FROM stripe_event WHERE stripe_event_id = #{stripeEventId} LIMIT 1")
    StripeEvent selectByStripeEventId(@Param("stripeEventId") String stripeEventId);
}
