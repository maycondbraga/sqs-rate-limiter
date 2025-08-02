package com.md.sqsratelimiter.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class ValidaLiberacaoMensagemUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ValidaLiberacaoMensagemUseCase.class);
    private final RateLimiterService rateLimiterService;

    public ValidaLiberacaoMensagemUseCase(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    public void execute(Message<MensagemLiberacaoDto> mensagem) {
        if (rateLimiterService.tryConsume()) {
            logger.info("✔️ Mensagem recebida: {}", mensagem.getPayload());
        }

        logger.error("❌ Rate limit excedido — mensagem será reprocessada.");
        throw new RuntimeException("Rate limit exceeded");
    }
}
