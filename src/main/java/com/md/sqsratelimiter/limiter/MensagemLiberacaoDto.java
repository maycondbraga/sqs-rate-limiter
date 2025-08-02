package com.md.sqsratelimiter.limiter;

import java.time.LocalDate;

public record MensagemLiberacaoDto(String id, String nome, LocalDate dataNascimento) {
}
