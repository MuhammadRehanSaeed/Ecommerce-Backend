package com.rehancode.ecommercebackend.RateLimiting;


import lombok.Data;

@Data
public class RateLimitingInfo {
    private int requestCount;
    private Long startWindow;

    public RateLimitingInfo(int i, long now) {
        this.requestCount=i;
        this.startWindow=now;
    }
}
