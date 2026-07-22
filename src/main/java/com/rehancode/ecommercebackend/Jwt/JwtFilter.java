package com.rehancode.ecommercebackend.Jwt;

import com.rehancode.ecommercebackend.Redis.RedisTokenBlacklistService;
import com.rehancode.ecommercebackend.Security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisTokenBlacklistService redisTokenBlacklistService;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService, RedisTokenBlacklistService redisTokenBlacklistService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.redisTokenBlacklistService = redisTokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            if (!token.isBlank()) {
               if( redisTokenBlacklistService.isBlacklist(token)){
                   log.warn("Blacklisted token used");
                   response.sendError(
                           HttpServletResponse.SC_UNAUTHORIZED,
                           "Token has been revoked"
                   );
                   return;
               }

                try {
                    username = jwtService.extractUsername(token);
                } catch (Exception e) {
                    log.warn("JWT token validation failed: {}", e.getMessage());
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (!userDetails.isEnabled()) {
                log.warn("User account is disabled username={}", username);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User account is non-enabled");
                return;
            }
            if (jwtService.validateToken(token, userDetails)) {
                log.debug("JWT token validated successfully username={}", username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request, response);
    }

}
