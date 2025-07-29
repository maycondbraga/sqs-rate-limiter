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
        rateLimit.set(limit);
    }

    public void setRefillIntervalSeconds(int seconds) {
        refillIntervalSeconds.set(seconds);
    }
}
