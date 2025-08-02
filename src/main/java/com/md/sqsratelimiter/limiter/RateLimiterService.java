package com.md.sqsratelimiter.limiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private static final int consume_tokens = 1;
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    private Bucket bucket;
    private final FeatureToggleService featureToggleService;

    private volatile int lastRateLimit;
    private volatile int lastRefillInterval;

    public RateLimiterService(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
        createBucket();
    }

    public boolean tryConsume() {
        updateIfNeeded();
        return bucket.tryConsume(consume_tokens);
    }

    private void createBucket() {
        RateLimitConfigDto config = featureToggleService.getConfig();
        createOrUpdateBucket(config.rateLimit(), config.refillIntervalSeconds());
    }

    private void updateIfNeeded() {
        RateLimitConfigDto config = featureToggleService.getConfig();

        if (config.rateLimit() != lastRateLimit || config.refillIntervalSeconds() != lastRefillInterval) {
            createOrUpdateBucket(config.rateLimit(), config.refillIntervalSeconds());
        }
    }

    private synchronized void createOrUpdateBucket(int rate, int interval) {
        Bandwidth limit = Bandwidth.classic(rate, Refill.greedy(rate, Duration.ofSeconds(interval)));
        this.bucket = Bucket.builder().addLimit(limit).build();

        this.lastRateLimit = rate;
        this.lastRefillInterval = interval;

        logger.info("Bucket atualizado: {} TPS a cada {} segundos", rate, interval);
    }
}
