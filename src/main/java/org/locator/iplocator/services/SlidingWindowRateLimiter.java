package org.locator.iplocator.services;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SlidingWindowRateLimiter {

    private final ConcurrentHashMap<String, RequestWindow> clientWindows = new ConcurrentHashMap<>();

    public boolean isAllowed(String clientId) {
        RequestWindow window = clientWindows.computeIfAbsent(clientId, k -> new RequestWindow());
        return window.allowRequest();
    }

    private int getMaxRequestsForCurrentMinute() {
        long currentMinute = Instant.now().getEpochSecond() / 60;
        return (currentMinute % 2 == 0) ? 80 : 40;
    }

    private static class RequestWindow {
        private final ConcurrentLinkedQueue<Long> requests = new ConcurrentLinkedQueue<>();
        private final AtomicInteger requestCount = new AtomicInteger(0);

        public synchronized boolean allowRequest() {
            long now = Instant.now().getEpochSecond();
            long currentMinute = now / 60;

            requests.removeIf(timestamp -> {
                long requestMinute = timestamp / 60;
                if (requestMinute < currentMinute) {
                    requestCount.decrementAndGet();
                    return true;
                }
                return false;
            });

            int maxRequests = getMaxRequestsForMinute(currentMinute);

            if (requestCount.get() < maxRequests) {
                requests.offer(now);
                requestCount.incrementAndGet();
                return true;
            }

            return false;
        }

        private int getMaxRequestsForMinute(long minute) {
            return (minute % 2 == 0) ? 80 : 40;
        }
    }
}
