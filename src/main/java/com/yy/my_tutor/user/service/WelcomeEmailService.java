package com.yy.my_tutor.user.service;

import com.yy.my_tutor.config.GoDaddyEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 注册/新增用户后的欢迎邮件，异步发送以免阻塞请求线程。
 */
@Slf4j
@Service
public class WelcomeEmailService {

    private static final String DEFAULT_LOGIN_LINK = "https://www.mytutor.top/loginNew";

    @Async("emailAsyncExecutor")
    public void sendWelcomeAsync(String email, String displayName) {
        if (!StringUtils.hasText(email)) {
            return;
        }
        try {
            GoDaddyEmailSender.sendWelcomeEmail(email, displayName, DEFAULT_LOGIN_LINK);
            log.info("欢迎邮件发送完成(异步): email={}", email);
        } catch (Exception e) {
            // 异步任务内记录异常，不向调用方抛出，避免被 AsyncUncaughtExceptionHandler 打日志重复
            log.error("欢迎邮件发送失败(异步): email={}, error={}", email, e.getMessage(), e);
        }
    }
}
