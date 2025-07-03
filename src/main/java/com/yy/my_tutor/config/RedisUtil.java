package com.yy.my_tutor.config;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil<T> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 存储数据
    public void set(String key, Object value, long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime);
    }

    // 获取数据
    public String get(String key) {

        Object obj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNull(obj)) {
            return null;
        } else {
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


