package com.md.sqsratelimiter.dto;

import java.time.LocalDate;

public record MensagemLiberacaoDto(String id, String nome, LocalDate dataNascimento) {
}
