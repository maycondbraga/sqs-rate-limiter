package com.md.sqsratelimiter.limiter;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FeatureToggleService {

    private final AtomicInteger rateLimit = new AtomicInteger(4);
    private final AtomicInteger refillIntervalSeconds = new AtomicInteger(1);
    private final AtomicInteger maxMessagesPerPoll = new AtomicInteger(2);
    private final AtomicInteger maxConcurrentMessages = new AtomicInteger(3);

    public RateLimitConfigDto getConfig() {
        return new RateLimitConfigDto(
                rateLimit.get(),
                refillIntervalSeconds.get(),
                maxMessagesPerPoll.get(),
                maxConcurrentMessages.get()
        );
    }

    public void updateConfig(int rate, int interval, int poll, int concurrent) {
        rateLimit.set(rate);
        refillIntervalSeconds.set(interval);
        maxMessagesPerPoll.set(poll);
        maxConcurrentMessages.set(concurrent);
    }
}
