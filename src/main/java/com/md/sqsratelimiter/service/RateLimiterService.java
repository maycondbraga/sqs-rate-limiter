package com.md.sqsratelimiter.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    private Bucket bucket;
    private final FeatureToggleService featureToggleService;

    private volatile int lastRateLimit;
    private volatile int lastRefillInterval;

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
            createOrUpdateBucket(currentRate, currentInterval);
        }
    }

    private synchronized void createOrUpdateBucket(int rate, int interval) {
        Bandwidth limit = Bandwidth.classic(rate, Refill.greedy(rate, Duration.ofSeconds(interval)));
        this.bucket = Bucket.builder().addLimit(limit).build();

        this.lastRateLimit = rate;
        this.lastRefillInterval = interval;

        logger.info("Bucket atualizado: {} TPS a cada {} segundos", rate, interval);
    }

    private void createOrUpdateBucket() {
        int rate = featureToggleService.getRateLimit();
        int interval = featureToggleService.getRefillIntervalSeconds();
        createOrUpdateBucket(rate, interval);
    }
}
