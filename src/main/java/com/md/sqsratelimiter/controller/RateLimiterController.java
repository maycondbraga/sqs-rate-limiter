package com.md.sqsratelimiter.controller;

import com.md.sqsratelimiter.service.FeatureToggleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/limiter")
public class RateLimiterController {

    private final FeatureToggleService toggleService;

    public RateLimiterController(FeatureToggleService toggleService) {
        this.toggleService = toggleService;
    }

    @PostMapping("/config")
    public ResponseEntity<String> updateRateLimit(@RequestParam int rateLimit, @RequestParam int refillSeconds) {
        toggleService.setRateLimit(rateLimit);
        toggleService.setRefillIntervalSeconds(refillSeconds);
        return ResponseEntity.ok("Rate limit updated");
    }

    @GetMapping("/config")
    public ResponseEntity<String> getRateLimitConfig() {
        return ResponseEntity.ok(String.format(
                "Rate limit: %d TPS, Reload interval: %d seconds",
                toggleService.getRateLimit(),
                toggleService.getRefillIntervalSeconds()
        ));
    }
}
