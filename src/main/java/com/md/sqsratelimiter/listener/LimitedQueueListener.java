package com.md.sqsratelimiter.listener;

import com.md.sqsratelimiter.service.RateLimiterService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
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

    @SqsListener(value = "${cloud.aws.sqs.queue.name}", acknowledgementMode = SqsListenerAcknowledgementMode.MANUAL)
    public void receiveMessage(String message, Acknowledgement ack) {
        if (rateLimiterService.tryConsume()) {
            logger.info("Received message: {}", message);
            ack.acknowledge();
        } else {
            logger.warn("Rate limit exceeded, message will remain in the queue: {}", message);
        }
    }
}
