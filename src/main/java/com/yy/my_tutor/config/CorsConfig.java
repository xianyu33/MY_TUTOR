package com.yy.my_tutor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 匹配所有接口
                .allowedOrigins("*")           // 允许所有来源（生产环境建议指定具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*")           // 允许所有请求头
                .allowCredentials(false)       // 是否允许发送 Cookie（需与 allowedOrigins 配合）
                .maxAge(3600);                  // 预检请求缓存时间（秒）
    }
}

