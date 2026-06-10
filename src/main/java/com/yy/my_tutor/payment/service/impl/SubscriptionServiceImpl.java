package com.yy.my_tutor.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.yy.my_tutor.payment.domain.dto.SubscriptionDTO;
import com.yy.my_tutor.payment.domain.entity.PaymentPrice;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.entity.PaymentSubscription;
import com.yy.my_tutor.payment.handler.SubscriptionUpsertHelper;
import com.yy.my_tutor.payment.mapper.PaymentPriceMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import com.yy.my_tutor.payment.service.StripeClientService;
import com.yy.my_tutor.payment.service.SubscriptionService;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import com.yy.my_tutor.payment.util.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private PaymentProductMapper productMapper;
    @Resource private PaymentPriceMapper priceMapper;
    @Resource private StripeClientService stripeClient;
    @Resource private SubscriptionUpsertHelper upsertHelper;
    @Resource private EntitlementCacheInvalidator cacheInvalidator;

    @Override
    public List<SubscriptionDTO> listMy(Integer payerUserId, String status, Integer beneficiaryStudentId) {
        QueryWrapper<PaymentSubscription> qw = new QueryWrapper<>();
        qw.eq("payer_user_id", payerUserId).eq("delete_flag", "N");
        if (status != null) qw.eq("status", status);
        if (beneficiaryStudentId != null) qw.eq("beneficiary_student_id", beneficiaryStudentId);
        qw.orderByDesc("create_at");
        return subscriptionMapper.selectList(qw).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public SubscriptionDTO getDetail(Integer subId, Integer payerUserId) {
        PaymentSubscription s = mustOwn(subId, payerUserId);
        return toDTO(s);
    }

    @Override
    public SubscriptionDTO cancelAtPeriodEnd(Integer subId, Integer payerUserId) {
        PaymentSubscription s = mustOwn(subId, payerUserId);
        try {
            Subscription remote = stripeClient.cancelSubscriptionAtPeriodEnd(s.getStripeSubscriptionId(), true);
            PaymentSubscription updated = upsertHelper.upsert(remote, System.currentTimeMillis()/1000L, remote.getMetadata());
            cacheInvalidator.invalidate(s.getBeneficiaryStudentId());
            return toDTO(updated != null ? updated : s);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    public SubscriptionDTO resume(Integer subId, Integer payerUserId) {
        PaymentSubscription s = mustOwn(subId, payerUserId);
        if (s.getCancelAtPeriodEnd() == null || s.getCancelAtPeriodEnd() == 0) {
            throw PaymentException.of("PAYMENT_SUB_NOT_CANCELABLE", "订阅未发起周期末取消,无需恢复");
        }
        try {
            Subscription remote = stripeClient.cancelSubscriptionAtPeriodEnd(s.getStripeSubscriptionId(), false);
            PaymentSubscription updated = upsertHelper.upsert(remote, System.currentTimeMillis()/1000L, remote.getMetadata());
            cacheInvalidator.invalidate(s.getBeneficiaryStudentId());
            return toDTO(updated != null ? updated : s);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    public SubscriptionDTO applyPaymentMethod(Integer subId, Integer payerUserId, String paymentMethodId) {
        PaymentSubscription s = mustOwn(subId, payerUserId);
        if (paymentMethodId == null || paymentMethodId.isEmpty()) {
            throw PaymentException.of("PAYMENT_METHOD_REQUIRED");
        }
        try {
            Subscription remote = stripeClient.updateSubscriptionDefaultPaymentMethod(s.getStripeSubscriptionId(), paymentMethodId);
            PaymentSubscription updated = upsertHelper.upsert(remote, System.currentTimeMillis()/1000L, remote.getMetadata());
            return toDTO(updated != null ? updated : s);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    @Override
    public SubscriptionDTO cancelNow(Integer subId, Integer operatorUserId) {
        PaymentSubscription s = subscriptionMapper.selectById(subId);
        if (s == null) throw PaymentException.of("PAYMENT_SUB_NOT_FOUND");
        try {
            Subscription remote = stripeClient.cancelSubscriptionImmediately(s.getStripeSubscriptionId());
            PaymentSubscription updated = upsertHelper.upsert(remote, System.currentTimeMillis()/1000L, remote.getMetadata());
            cacheInvalidator.invalidate(s.getBeneficiaryStudentId());
            log.info("Subscription {} canceled immediately by admin {}", s.getStripeSubscriptionId(), operatorUserId);
            return toDTO(updated != null ? updated : s);
        } catch (StripeException e) {
            throw PaymentException.of("PAYMENT_STRIPE_UNAVAILABLE", e.getMessage());
        }
    }

    private PaymentSubscription mustOwn(Integer subId, Integer payerUserId) {
        PaymentSubscription s = subscriptionMapper.selectById(subId);
        if (s == null) throw PaymentException.of("PAYMENT_SUB_NOT_FOUND");
        if (!payerUserId.equals(s.getPayerUserId())) {
            throw PaymentException.of("PAYMENT_SUBSCRIPTION_NOT_OWNED");
        }
        return s;
    }

    private SubscriptionDTO toDTO(PaymentSubscription s) {
        PaymentProduct product = productMapper.selectById(s.getProductId());
        PaymentPrice price = priceMapper.selectById(s.getPriceId());
        return SubscriptionDTO.of(
                s,
                product == null ? null : product.getName(),
                price == null ? null : price.getUnitAmount(),
                price == null ? null : price.getBillingInterval());
    }
}
