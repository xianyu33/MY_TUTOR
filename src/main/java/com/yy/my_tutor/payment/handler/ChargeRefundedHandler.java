package com.yy.my_tutor.payment.handler;

import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentRefund;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentRefundMapper;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Component
public class ChargeRefundedHandler implements EventHandler {

    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentRefundMapper refundMapper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override public String eventType() { return "charge.refunded"; }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HandlerResult handle(Event event) {
        Charge charge = EventObjectExtractor.get(event, Charge.class);
        if (charge == null) return HandlerResult.FAILED;

        if (charge.getRefunds() != null && charge.getRefunds().getData() != null) {
            for (Refund remote : charge.getRefunds().getData()) {
                PaymentRefund local = refundMapper.selectByStripeId(remote.getId());
                if (local == null) {
                    local = new PaymentRefund();
                    local.setStripeRefundId(remote.getId());
                    local.setAmount(remote.getAmount());
                    local.setCurrency(remote.getCurrency());
                    local.setReason(remote.getReason());
                    local.setStatus(remote.getStatus());
                    local.setOperatorUserId(0);
                    local.setOrderId(0);
                    local.setCreateBy("STRIPE_WEBHOOK");
                    local.setUpdateBy("STRIPE_WEBHOOK");
                    refundMapper.insert(local);
                } else {
                    local.setStatus(remote.getStatus());
                    refundMapper.updateById(local);
                }
            }
        }

        PaymentOrder order = orderMapper.selectByPaymentIntentId(charge.getPaymentIntent());
        if (order == null) return HandlerResult.SKIPPED;
        Long refundedTotal = charge.getAmountRefunded() == null ? 0L : charge.getAmountRefunded();
        order.setStatus(refundedTotal >= order.getAmount() ? OrderStatus.REFUNDED.name() : OrderStatus.PARTIALLY_REFUNDED.name());
        orderMapper.updateById(order);

        if (charge.getRefunds() != null && charge.getRefunds().getData() != null) {
            for (Refund remote : charge.getRefunds().getData()) {
                PaymentRefund local = refundMapper.selectByStripeId(remote.getId());
                if (local != null && (local.getOrderId() == null || local.getOrderId() == 0)) {
                    local.setOrderId(order.getId());
                    refundMapper.updateById(local);
                }
            }
        }

        cacheInvalidator.invalidate(order.getBeneficiaryStudentId());
        log.info("Order {} → {} (refunded {} / {})", order.getOrderNo(), order.getStatus(), refundedTotal, order.getAmount());
        return HandlerResult.SUCCESS;
    }
}
