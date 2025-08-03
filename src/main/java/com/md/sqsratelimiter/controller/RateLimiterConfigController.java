package com.md.sqsratelimiter.controller;

import com.md.sqsratelimiter.dto.RateLimitConfigDto;
import com.md.sqsratelimiter.service.FeatureToggleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class RateLimiterConfigController {

    private final FeatureToggleService toggleService;

    public RateLimiterConfigController(FeatureToggleService toggleService) {
        this.toggleService = toggleService;
    }

    @GetMapping
    public RateLimitConfigDto getConfig() {
        return toggleService.getConfig();
    }

    @PostMapping
    public ResponseEntity<String> updateConfig(@RequestParam int rateLimit, @RequestParam int refillSeconds, @RequestParam int maxMessagesPerPoll, @RequestParam int maxConcurrentMessages) {
        toggleService.updateConfig(rateLimit, refillSeconds, maxMessagesPerPoll, maxConcurrentMessages);
        return ResponseEntity.ok("\uD83D\uDCA1 Configuração atualizada com sucesso");
    }
}
