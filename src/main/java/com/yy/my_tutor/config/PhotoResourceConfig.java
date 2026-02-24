package com.yy.my_tutor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 头像等图片静态资源映射：/tutor/photo/** 映射到本地保存目录。
 */
@Configuration
public class PhotoResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:tutor/photo}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File dir = new File(uploadPath);
        if (!dir.isAbsolute()) {
            dir = new File(System.getProperty("user.dir"), uploadPath);
        }
        String location = dir.getAbsolutePath();
        if (!location.endsWith(File.separator)) {
            location += File.separator;
        }
        registry.addResourceHandler("/tutor/photo/**")
                .addResourceLocations("file:" + location);
    }
}
