package com.md.sqsratelimiter.dto;

public record RateLimitConfigDto(int rateLimit, int refillIntervalSeconds, int maxMessagesPerPoll, int maxConcurrentMessages) {
}
