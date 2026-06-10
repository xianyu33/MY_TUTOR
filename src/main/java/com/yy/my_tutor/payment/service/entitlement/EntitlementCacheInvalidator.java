package com.yy.my_tutor.payment.service.entitlement;

import com.yy.my_tutor.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class EntitlementCacheInvalidator {

    public static final String KEY_PREFIX = "entitlement:student:";

    @Resource
    private RedisUtil redisUtil;

    public void invalidate(Integer studentId) {
        if (studentId == null) return;
        String baseKey = KEY_PREFIX + studentId;
        try {
            redisUtil.delete(baseKey);
            redisUtil.delete(baseKey + ":sub");
            log.debug("Invalidated entitlement cache for student {}", studentId);
        } catch (Exception e) {
            log.warn("Failed to invalidate entitlement cache for student {}: {}", studentId, e.getMessage());
        }
    }
}
