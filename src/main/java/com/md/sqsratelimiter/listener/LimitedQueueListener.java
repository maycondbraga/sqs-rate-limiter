package com.md.sqsratelimiter.listener;

import com.md.sqsratelimiter.service.RateLimiterService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class LimitedQueueListener {
    RateLimiterService rateLimiterService;

    public LimitedQueueListener(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @SqsListener(value = "${cloud.aws.sqs.queue.name}", acknowledgementMode = "ON_SUCCESS")
    public void receiveMessage(String message) {
        if(rateLimiterService.tryConsume()) {
            System.out.println("Received message: " + message);
        } else {
            System.out.println("Rate limit exceeded, message not processed: " + message);
            throw new RuntimeException("Rate limit exceeded");
        }
    }
}
