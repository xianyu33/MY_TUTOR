package com.yy.my_tutor.payment.util;

import com.yy.my_tutor.payment.config.StripeConfig;
import com.yy.my_tutor.user.domain.User;
import com.yy.my_tutor.user.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 从 SecurityContext 取出登录用户 ID。
 *
 * 项目现状:JwtAuthenticationTokenFilter 把 username 作为 principal(String),
 * 因此需要二次查 user 表拿 id。
 */
@Component
public class PaymentSecurityUtil {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StripeConfig stripeConfig;

    /** 获取当前登录 user.id,无登录抛 PAYMENT_UNAUTHORIZED */
    public Integer currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null || "anonymousUser".equals(auth.getPrincipal())) {
            if (stripeConfig.isLocalAuthBypassEnabled()) {
                return stripeConfig.getLocalAuthBypass().getUserId();
            }
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "请先登录");
        }
        String username = auth.getPrincipal().toString();
        User u = userMapper.findByUserAccount(username);
        if (u == null || u.getId() == null) {
            throw PaymentException.of("PAYMENT_UNAUTHORIZED", "用户不存在");
        }
        return u.getId();
    }

    /** 当前用户必须是 admin(stripe.admin-user-ids 白名单);否则抛 PAYMENT_FORBIDDEN */
    public void requireAdmin() {
        if (stripeConfig.isLocalAuthBypassAdmin()) {
            return;
        }
        Integer uid = currentUserId();
        if (!stripeConfig.isAdmin(uid)) {
            throw PaymentException.of("PAYMENT_FORBIDDEN", "管理员权限不足");
        }
    }
}
