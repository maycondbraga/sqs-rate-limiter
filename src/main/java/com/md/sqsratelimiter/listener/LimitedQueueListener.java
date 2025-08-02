package com.md.sqsratelimiter.listener;

import com.md.sqsratelimiter.exception.RateLimitExceededException;
import com.md.sqsratelimiter.service.RateLimiterService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LimitedQueueListener {
    private static final Logger logger = LoggerFactory.getLogger(LimitedQueueListener.class);

    private final RateLimiterService rateLimiterService;

    public LimitedQueueListener(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @SqsListener(value = "${cloud.aws.sqs.queue.name}", acknowledgementMode = "ON_SUCCESS")
    public void receiveMessage(String message) {
        if (rateLimiterService.tryConsume()) {
            logger.info("Received message: {}", message);
        } else {
            logger.warn("Rate limit exceeded, message not processed: {}", message);
            throw new RateLimitExceededException("Rate limit exceeded");
        }
    }
}
