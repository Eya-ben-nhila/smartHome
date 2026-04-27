package com.smarthome.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    // Fallback in-memory rate limiting when Redis is not available
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastResetTime = new ConcurrentHashMap<>();

    // Rate limits per endpoint type
    private static final int AUTH_RATE_LIMIT = 5; // 5 requests per minute
    private static final int DEVICE_RATE_LIMIT = 100; // 100 requests per minute
    private static final int ENERGY_RATE_LIMIT = 1000; // 1000 requests per minute
    private static final int DEFAULT_RATE_LIMIT = 60; // 60 requests per minute
    private static final Duration RATE_WINDOW = Duration.ofMinutes(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);
        String endpoint = getEndpointType(request.getRequestURI());
        String key = clientIp + ":" + endpoint;

        if (isRateLimited(key, endpoint)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"timestamp\":\"%s\",\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded for endpoint: %s\",\"path\":\"%s\"}",
                java.time.LocalDateTime.now(),
                endpoint,
                request.getRequestURI()
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimited(String key, String endpoint) {
        if (redisTemplate != null) {
            return isRateLimitedWithRedis(key, endpoint);
        } else {
            return isRateLimitedInMemory(key, endpoint);
        }
    }

    private boolean isRateLimitedWithRedis(String key, String endpoint) {
        try {
            Long currentCount = redisTemplate.opsForValue().increment(key);
            if (currentCount == 1) {
                // Set expiration for new key
                redisTemplate.expire(key, RATE_WINDOW);
            }

            int limit = getRateLimit(endpoint);
            return currentCount > limit;
        } catch (Exception e) {
            // Fallback to in-memory if Redis fails
            return isRateLimitedInMemory(key, endpoint);
        }
    }

    private boolean isRateLimitedInMemory(String key, String endpoint) {
        long currentTime = System.currentTimeMillis();
        
        // Reset counter if window has expired
        Long lastReset = lastResetTime.get(key);
        if (lastReset == null || currentTime - lastReset > RATE_WINDOW.toMillis()) {
            requestCounts.put(key, new AtomicInteger(0));
            lastResetTime.put(key, currentTime);
        }

        AtomicInteger count = requestCounts.get(key);
        int currentCount = count.incrementAndGet();
        
        int limit = getRateLimit(endpoint);
        return currentCount > limit;
    }

    private int getRateLimit(String endpoint) {
        return switch (endpoint) {
            case "auth" -> AUTH_RATE_LIMIT;
            case "devices" -> DEVICE_RATE_LIMIT;
            case "energy" -> ENERGY_RATE_LIMIT;
            default -> DEFAULT_RATE_LIMIT;
        };
    }

    private String getEndpointType(String requestUri) {
        if (requestUri.contains("/auth/")) {
            return "auth";
        } else if (requestUri.contains("/devices")) {
            return "devices";
        } else if (requestUri.contains("/energy")) {
            return "energy";
        } else {
            return "default";
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
