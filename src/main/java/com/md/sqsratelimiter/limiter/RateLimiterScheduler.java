package com.md.sqsratelimiter.limiter;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterScheduler {

    private final FeatureToggleService toggleService;
    private final ApplicationEventPublisher publisher;
    private RateLimitConfigDto lastConfig = null;

    public RateLimiterScheduler(FeatureToggleService toggleService, ApplicationEventPublisher publisher) {
        this.toggleService = toggleService;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 5000)
    public void checkForConfigChanges() {
        RateLimitConfigDto current = toggleService.getConfig();
        if (!current.equals(lastConfig)) {
            lastConfig = current;
            publisher.publishEvent(new RateLimitChangedEventDto(current));
        }
    }
}
