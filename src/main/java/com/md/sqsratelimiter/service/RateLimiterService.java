package com.md.sqsratelimiter.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class RateLimiterService {

    private volatile Bucket bucket;

    private final FeatureToggleService featureToggleService;

    private int lastRateLimit;
    private int lastRefillInterval;

    public RateLimiterService(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
        createOrUpdateBucket();
    }

    public boolean tryConsume() {
        updateIfNeeded();
        return bucket.tryConsume(1);
    }

    private void updateIfNeeded() {
        int currentRate = featureToggleService.getRateLimit();
        int currentInterval = featureToggleService.getRefillIntervalSeconds();

        if (currentRate != lastRateLimit || currentInterval != lastRefillInterval) {
            createOrUpdateBucket();
        }
    }

    private synchronized void createOrUpdateBucket() {
        int rate = featureToggleService.getRateLimit();
        int interval = featureToggleService.getRefillIntervalSeconds();

        Bandwidth limit = Bandwidth.classic(rate, Refill.greedy(rate, Duration.ofSeconds(interval)));
        this.bucket = Bucket.builder().addLimit(limit).build();

        this.lastRateLimit = rate;
        this.lastRefillInterval = interval;

        System.out.printf("Bucket atualizado: %d TPS a cada %d segundos%n", rate, interval);
    }
}
