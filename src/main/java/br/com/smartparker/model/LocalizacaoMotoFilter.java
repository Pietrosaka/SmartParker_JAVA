package br.com.smartparker.model;

import java.time.LocalDateTime;

public record LocalizacaoMotoFilter(LocalDateTime dataInicio, LocalDateTime dataFim, Moto moto, Setor setor) {
}
