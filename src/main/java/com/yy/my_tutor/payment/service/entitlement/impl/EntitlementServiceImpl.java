package com.yy.my_tutor.payment.service.entitlement.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yy.my_tutor.config.RedisUtil;
import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.payment.domain.entity.PaymentOrder;
import com.yy.my_tutor.payment.domain.entity.PaymentProduct;
import com.yy.my_tutor.payment.domain.enums.OrderStatus;
import com.yy.my_tutor.payment.mapper.PaymentOrderMapper;
import com.yy.my_tutor.payment.mapper.PaymentProductMapper;
import com.yy.my_tutor.payment.mapper.PaymentSubscriptionMapper;
import com.yy.my_tutor.payment.service.entitlement.EntitlementCacheInvalidator;
import com.yy.my_tutor.payment.service.entitlement.EntitlementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EntitlementServiceImpl implements EntitlementService {

    @Resource private PaymentSubscriptionMapper subscriptionMapper;
    @Resource private PaymentOrderMapper orderMapper;
    @Resource private PaymentProductMapper productMapper;
    @Resource private RedisUtil redisUtil;
    @Resource private StripeConfig stripeConfig;

    @Override
    public boolean hasActiveSubscription(Integer studentId) {
        if (studentId == null) return false;
        String cacheKey = EntitlementCacheInvalidator.KEY_PREFIX + studentId + ":sub";
        Object cached = redisUtil.get(cacheKey);
        if (cached != null) return Boolean.parseBoolean(cached.toString());
        boolean has = subscriptionMapper.countActiveByStudent(studentId) > 0;
        try {
            redisUtil.set(cacheKey, Boolean.toString(has), stripeConfig.getEntitlementCache().getTtlSeconds());
        } catch (Exception ignore) {}
        return has;
    }

    @Override
    public boolean canAccessCourse(Integer studentId, Integer courseId) {
        if (studentId == null || courseId == null) return false;
        if (hasActiveSubscription(studentId)) return true;
        return hasOneTimePurchase(studentId, "COURSE", courseId);
    }

    @Override
    public boolean canAccessKnowledgePoint(Integer studentId, Integer knowledgePointId) {
        if (studentId == null || knowledgePointId == null) return false;
        if (hasActiveSubscription(studentId)) return true;
        return hasOneTimePurchase(studentId, "KNOWLEDGE_PACK", knowledgePointId);
    }

    @Override
    public boolean canAccessTestPack(Integer studentId, Integer testPackId) {
        if (studentId == null || testPackId == null) return false;
        if (hasActiveSubscription(studentId)) return true;
        return hasOneTimePurchase(studentId, "TEST_PACK", testPackId);
    }

    private boolean hasOneTimePurchase(Integer studentId, String productType, Integer targetRefId) {
        QueryWrapper<PaymentProduct> pqw = new QueryWrapper<>();
        pqw.eq("product_type", productType).eq("target_ref_id", targetRefId).eq("delete_flag", "N");
        List<PaymentProduct> products = productMapper.selectList(pqw);
        if (products.isEmpty()) return false;
        List<Integer> productIds = products.stream().map(PaymentProduct::getId)
                .collect(java.util.stream.Collectors.toList());

        QueryWrapper<PaymentOrder> oqw = new QueryWrapper<>();
        oqw.eq("beneficiary_student_id", studentId)
           .eq("status", OrderStatus.PAID.name())
           .in("product_id", productIds)
           .eq("delete_flag", "N");
        return orderMapper.selectCount(oqw) > 0;
    }
}
