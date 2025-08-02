package com.md.sqsratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SqsRateLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqsRateLimiterApplication.class, args);
    }

}
