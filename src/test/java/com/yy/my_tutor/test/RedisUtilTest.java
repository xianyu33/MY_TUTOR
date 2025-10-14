package com.yy.my_tutor.test;

import com.yy.my_tutor.config.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * RedisUtil测试类
 */
@SpringBootTest
public class RedisUtilTest {

    @Autowired
    private RedisUtil<String> redisUtil;

    @Test
    public void testStringStorage() {
        String testKey = "test:captcha";
        String testValue = "1234";
        
        // 存储字符串
        redisUtil.set(testKey, testValue, 60);
        
        // 获取字符串
        String retrievedValue = redisUtil.get(testKey);
        
        System.out.println("原始值: " + testValue);
        System.out.println("获取值: " + retrievedValue);
        System.out.println("是否相等: " + testValue.equals(retrievedValue));
        
        // 清理
        redisUtil.delete(testKey);
    }
}
