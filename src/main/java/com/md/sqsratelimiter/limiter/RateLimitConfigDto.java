package com.md.sqsratelimiter.limiter;

public record RateLimitConfigDto(int rateLimit, int refillIntervalSeconds, int maxMessagesPerPoll, int maxConcurrentMessages) {
}
