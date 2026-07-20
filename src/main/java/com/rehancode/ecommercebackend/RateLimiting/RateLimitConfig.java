package com.rehancode.ecommercebackend.RateLimiting;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RateLimitConfig {


    private final Map<String, RateLimitRule> rules = Map.of(

            "/api/auth",
            new RateLimitRule(5,60000),

            "/api/public",
            new RateLimitRule(30,60000),

            "/api/admin",
            new RateLimitRule(50,60000),

            "/api/user",
            new RateLimitRule(100,60000),

            "/api/seller",
            new RateLimitRule(50,60000)

    );



    public RateLimitRule getRule(String path){

        return rules.entrySet()
                .stream()
                .filter(entry -> path.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(
                        new RateLimitRule(20,60000)
                );

    }


    public boolean isPublic(String path){

        return path.startsWith("/api/auth")
                ||
                path.startsWith("/api/public");

    }

}