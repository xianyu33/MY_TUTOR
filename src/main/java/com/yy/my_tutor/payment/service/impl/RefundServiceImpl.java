package com.yy.my_tutor.payment.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.yy.my_tutor.payment.domain.dto.RefundRequest;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentRefundMapper;
import com.yy.my_tutor.payment.service.RefundService;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.util.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RefundServiceImpl implements RefundService {

    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentRefundMapper refundMapper;
    @Resource private StripeClientService stripeClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRefund refundOrder(String orderNo, RefundRequest req, Integer operatorUserId) {
        PaymentOrder order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) throw PaymentException.of("PAYMENT_ORDER_NOT_FOUND");
        if (!OrderStatus.PAID.name().equals(order.getStatus())
                && !OrderStatus.PARTIALLY_REFUNDED.name().equals(order.getStatus())) {
            throw PaymentException.of("PAYMENT_ORDER_NOT_REFUNDABLE");
        }
        if (order.getStripePaymentIntentId() == null) {
            throw PaymentException.of("PAYMENT_ORDER_NO_PAYMENT_INTENT");
        }
        Long amount = req.getAmount() == null ? order.getAmount() : req.getAmount();
        if (amount <= 0 || amount > order.getAmount()) {
            throw PaymentException.of("PAYMENT_REFUND_AMOUNT_INVALID");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("operator_user_id", String.valueOf(operatorUserId));
        if (req.getReasonDetail() != null) metadata.put("reason_detail", req.getReasonDetail());

        Refund stripeRefund;
        try {
            stripeRefund = stripeClient.createRefund(order.getStripePaymentIntentId(), amount, req.getReason(), metadata);
        } catch (StripeException e) {
            log.error("Stripe refund failed for order {}", orderNo, e);
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }

        PaymentRefund local = new PaymentRefund();
        local.setOrderId(order.getId());
        local.setStripeRefundId(stripeRefund.getId());
        local.setAmount(amount);
        local.setCurrency(order.getCurrency());
        local.setReason(req.getReason());
        local.setReasonDetail(req.getReasonDetail());
        local.setStatus(stripeRefund.getStatus());
        local.setOperatorUserId(operatorUserId);
        local.setCreateBy(String.valueOf(operatorUserId));
        local.setUpdateBy(String.valueOf(operatorUserId));
        refundMapper.insert(local);
        log.info("Refund created stripeId={} order={} amount={}", stripeRefund.getId(), orderNo, amount);
        return local;
    }
}
