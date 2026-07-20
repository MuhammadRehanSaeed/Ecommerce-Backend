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

    public RateLimitFilter(
            RateLimitService service,
            RateLimitConfig config
    ) {
        this.service = service;
        this.config = config;
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
            key = request.getRemoteAddr();
        } else {
            key = String.valueOf(SecurityUtils.getAuthenticatedUserId());
        }

        RateLimitRule rule = config.getRule(path);
        boolean allowed = service.allowRequests(key, rule);

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