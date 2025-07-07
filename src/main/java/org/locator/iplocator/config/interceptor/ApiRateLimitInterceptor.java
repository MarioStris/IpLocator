package org.locator.iplocator.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.locator.iplocator.services.SlidingWindowRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class ApiRateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private SlidingWindowRateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!request.getRequestURI().contains("/lookup/")) {
            return true;
        }

        String clientId = getClientIdentifier(request);

        if (!rateLimiter.isAllowed(clientId)) {
            response.setStatus(429); // Too Many Requests

            log.warn("Rate limit exceeded for client: {} on lookup endpoint", clientId);
            return false;
        }

        return true;
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private long getNextMinuteTimestamp() {
        return (System.currentTimeMillis() / 60000 + 1) * 60;
    }
}
