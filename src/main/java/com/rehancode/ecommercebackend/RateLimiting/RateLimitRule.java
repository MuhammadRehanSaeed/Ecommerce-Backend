package com.rehancode.ecommercebackend.RateLimiting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitRule {

    private int limit;

    private long window;

}