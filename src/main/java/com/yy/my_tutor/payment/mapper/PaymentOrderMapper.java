package com.yy.my_tutor.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrder> {

    @Select("SELECT * FROM payment_order WHERE order_no = #{orderNo} AND delete_flag = 'N' LIMIT 1")
    PaymentOrder selectByOrderNo(@Param("orderNo") String orderNo);

    @Select("SELECT * FROM payment_order WHERE stripe_checkout_session_id = #{sessionId} AND delete_flag = 'N' LIMIT 1")
    PaymentOrder selectByStripeSessionId(@Param("sessionId") String sessionId);

    @Select("SELECT * FROM payment_order WHERE stripe_payment_intent_id = #{paymentIntentId} AND delete_flag = 'N' LIMIT 1")
    PaymentOrder selectByPaymentIntentId(@Param("paymentIntentId") String paymentIntentId);

    @Select("SELECT * FROM payment_order WHERE stripe_invoice_id = #{invoiceId} AND delete_flag = 'N' LIMIT 1")
    PaymentOrder selectByInvoiceId(@Param("invoiceId") String invoiceId);
}
