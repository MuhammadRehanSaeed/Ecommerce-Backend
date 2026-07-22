package com.rehancode.ecommercebackend.RateLimiting;

import com.rehancode.ecommercebackend.Utils.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService service;
    private final RateLimitConfig config;
    private final RedisRateLimitiService redisRateLimitiService;

    public RateLimitFilter(
            RateLimitService service,
            RateLimitConfig config, RedisRateLimitiService redisRateLimitiService
    ) {
        this.service = service;
        this.config = config;
        this.redisRateLimitiService = redisRateLimitiService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        String path = request.getRequestURI();
        String key;

        if (config.isPublic(path)) {
            key = "ip:" + request.getRemoteAddr();
        } else {
//            key = String.valueOf(SecurityUtils.getAuthenticatedUserId());
            key = "user:" + SecurityUtils.getAuthenticatedUserId();
        }

        RateLimitRule rule = config.getRule(path);
        boolean allowed = service.allowRequests(key, rule);
        boolean allowed1=redisRateLimitiService.allowRequest(key, rule);

        if(!allowed1){
            log.warn("Rate limit exceeded path={}, key={}", path, key);
            response.setStatus(429);
            response.getWriter().write("""
                    {
                    "message":"Too Many Requests"
                    }
                    """);
            return;
        }
        if (!allowed) {
            log.warn("Rate limit exceeded path={}, key={}", path, key);
            response.setStatus(429);
            response.getWriter().write("""
                    {
                    "message":"Too Many Requests"
                    }
                    """);
            return;
        }

        filterChain.doFilter(request, response);
    }

}