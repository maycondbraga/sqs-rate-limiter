package com.md.sqsratelimiter.limiter;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class ValidaLiberacaoMensagemUseCase {
    public void execute(Message<MensagemLiberacaoDto> mensagem) {
        System.out.println("🔎 Mensagem recebida: " + mensagem);
    }
}
