package com.yy.my_tutor.user.service;

import com.yy.my_tutor.config.GoDaddyEmailSender;
import com.yy.my_tutor.config.RedisUtil;
import com.yy.my_tutor.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Service
public class EmailVerificationService {

    private static final String VERIFY_LINK_PREFIX = "https://www.mytutor.top/verify-email?code=";
    private static final String REDIS_KEY_PREFIX = "EMAIL_VERIFY:";
    private static final long TOKEN_EXPIRE_SECONDS = 24 * 60 * 60;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    public String generateVerificationToken(String email) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String redisKey = REDIS_KEY_PREFIX + token;
        redisUtil.set(redisKey, email, TOKEN_EXPIRE_SECONDS);
        log.info("生成邮箱校验token: email={}, token={}", email, token);
        return token;
    }

    public String getVerificationLink(String email) {
        String token = generateVerificationToken(email);
        return VERIFY_LINK_PREFIX + token;
    }

    public boolean verifyEmail(String token) {
        if (!StringUtils.hasText(token)) {
            log.warn("校验token为空");
            return false;
        }

        String redisKey = REDIS_KEY_PREFIX + token;
        String email = redisUtil.get(redisKey);
        if (!StringUtils.hasText(email)) {
            log.warn("校验token无效或已过期: token={}", token);
            return false;
        }

        userMapper.updateEmailVerified(email, 1);
        redisUtil.delete(redisKey);
        log.info("邮箱校验成功: email={}", email);
        return true;
    }

    @Async("emailAsyncExecutor")
    public void sendVerificationEmailAsync(String email, String displayName) {
        log.info("开始异步发送校验邮件: email={}", email);
        if (!StringUtils.hasText(email)) {
            return;
        }
        try {
            String verificationLink = getVerificationLink(email);
            GoDaddyEmailSender.sendVerificationEmail(email, displayName, verificationLink);
            log.info("校验邮件发送完成(异步): email={}", email);
        } catch (Exception e) {
            log.error("校验邮件发送失败(异步): email={}, error={}", email, e.getMessage(), e);
        }
    }
}