package org.locator.iplocator.config.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import org.locator.iplocator.validation.ValidationException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitInterceptor implements ClientHttpRequestInterceptor {

    private final RateLimiter rateLimiter = RateLimiter.create(1.0);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        boolean allowed = rateLimiter.tryAcquire();
        if (!allowed) {
            throw new ValidationException("Rate limit exceeded: Max 1 request per second.");
        }
        return execution.execute(request, body);
    }

}