package com.rehancode.ecommercebackend.RateLimiting;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisRateLimitiService {
    private final RedisTemplate<String,Object> redisTemplate;

    public RedisRateLimitiService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String key,RateLimitRule rule){

        String redisKey="rateLimit:"+key;

        Long count=redisTemplate.opsForValue().increment(redisKey);
        if(count==1){
            redisTemplate.expire(key,rule.getWindow(), TimeUnit.MILLISECONDS);
        }
        return count<=rule.getLimit();
    }
}
