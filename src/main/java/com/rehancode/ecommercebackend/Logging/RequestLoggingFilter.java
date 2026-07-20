package com.rehancode.ecommercebackend.Logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that logs all HTTP requests and responses in structured JSON format.
 * Populates MDC (Mapped Diagnostic Context) with request metadata so every
 * log line produced during a request automatically carries context fields that
 * Logstash / Kibana can index and search.
 *
 * MDC fields added per request:
 *   requestId  – unique UUID per request (use for request tracing)
 *   ip         – client IP address
 *   method     – HTTP verb
 *   path       – request URI
 *   userAgent  – caller's User-Agent header
 *   username   – authenticated principal (or "anonymous")
 *   statusCode – HTTP response status (set after execution)
 *   duration   – elapsed time in ms
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    // Headers to look through for the real client IP (proxy-aware)
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        // Populate MDC fields before the request executes
        MDC.put("requestId", requestId);
        MDC.put("ip", resolveClientIp(request));
        MDC.put("method", request.getMethod());
        MDC.put("path", request.getRequestURI());
        MDC.put("userAgent", nullSafe(request.getHeader("User-Agent")));

        // Add request ID to response so clients/load-balancers can correlate logs
        response.setHeader("X-Request-Id", requestId);

        try {
            log.info("Incoming request");
            filterChain.doFilter(request, response);
        } finally {
            // Resolve username after the security filter chain has run
            String username = resolveUsername();
            long duration = System.currentTimeMillis() - startTime;

            MDC.put("username", username);
            MDC.put("statusCode", String.valueOf(response.getStatus()));
            MDC.put("duration", duration + "ms");

            log.info("Request completed");

            // Always clear MDC to avoid leaking values across threads (thread pool reuse)
            MDC.clear();
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private String resolveClientIp(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For may contain a comma-separated chain; take the first
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    private String resolveUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()
                    && !"anonymousUser".equals(auth.getPrincipal())) {
                return auth.getName();
            }
        } catch (Exception ignored) {
            // SecurityContext might not be available in edge cases
        }
        return "anonymous";
    }

    private String nullSafe(String value) {
        return value != null ? value : "unknown";
    }
}
