package com.yy.my_tutor.config;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<T> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 存储数据
    public void set(String key, Object value, long expireTime) {
        if (value != null) {
            // 如果是字符串，直接存储，避免不必要的处理
            if (value instanceof String) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                // 其他类型进行JSON序列化后存储
                String jsonValue = JSON.toJSONString(value);
                redisTemplate.opsForValue().set(key, jsonValue, expireTime, TimeUnit.SECONDS);
            }
        }
    }

    // 获取数据
    public String get(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNull(obj)) {
            return null;
        } else {
            // 如果对象是字符串，直接返回，避免JSON序列化
            if (obj instanceof String) {
                return (String) obj;
            }
            // 其他类型才进行JSON序列化
            return JSON.toJSONString(obj);
        }
    }

    // 存储Hash数据
    public void setHash(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    // 删除Key
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    // 删除Key
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}


