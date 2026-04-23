package com.yy.my_tutor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "courseTranslationExecutor")
    public Executor courseTranslationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("course-translate-");
        executor.initialize();
        return executor;
    }

    /**
     * 欢迎邮件等 SMTP 操作耗时长，用独立小线程池与业务翻译等任务隔离。
     */
    @Bean(name = "emailAsyncExecutor")
    public Executor emailAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("email-welcome-");
        executor.initialize();
        return executor;
    }
}
