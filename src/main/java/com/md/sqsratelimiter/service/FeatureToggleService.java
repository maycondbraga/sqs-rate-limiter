package com.md.sqsratelimiter.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FeatureToggleService {

    private final AtomicInteger rateLimit = new AtomicInteger(10);
    private final AtomicInteger refillIntervalSeconds = new AtomicInteger(1);

    public int getRateLimit() {
        return rateLimit.get();
    }

    public int getRefillIntervalSeconds() {
        return refillIntervalSeconds.get();
    }

    public void setRateLimit(int limit) {
        if (limit <= 0) throw new IllegalArgumentException("Rate limit must be positive");
        rateLimit.set(limit);
    }

    public void setRefillIntervalSeconds(int seconds) {
        if (seconds <= 0) throw new IllegalArgumentException("Refill interval must be positive");
        refillIntervalSeconds.set(seconds);
    }
}
