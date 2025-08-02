package com.md.sqsratelimiter.limiter;

import org.springframework.stereotype.Service;

@Service
public class ValidaLiberacaoMensagemUseCase {
    public void execute(MensagemLiberacaoDto dto) {
        System.out.println("🔎 Mensagem recebida: " + dto);
    }
}
