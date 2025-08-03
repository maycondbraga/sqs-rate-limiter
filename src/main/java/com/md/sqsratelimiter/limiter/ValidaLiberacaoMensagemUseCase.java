package com.md.sqsratelimiter.limiter;

import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementCallback;
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

    public void execute(Message<MensagemLiberacaoDto> mensagem, AcknowledgementCallback<MensagemLiberacaoDto> ack) {
        if (rateLimiterService.tryConsume()) {
            logger.info("✔️ Mensagem recebida. Payload: {}", mensagem.getPayload());
            ack.onAcknowledge(mensagem);
        } else {
            logger.warn("❌ Rate limit excedido. Payload: {}", mensagem.getPayload());
        }
    }
}
