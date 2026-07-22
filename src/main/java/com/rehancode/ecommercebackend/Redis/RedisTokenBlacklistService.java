package com.rehancode.ecommercebackend.Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenBlacklistService {
    private final RedisTemplate<String,Object> redisTemplate;
    public RedisTokenBlacklistService(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void blacklist(String token,Long expireTime){

        String key="blacklist:"+token;
        redisTemplate.opsForValue().set(key,"true",expireTime, TimeUnit.MILLISECONDS);
    }
    public boolean isBlacklist(String token){
        String key="blacklist:"+token;
        return redisTemplate.hasKey(key);
    }
}
