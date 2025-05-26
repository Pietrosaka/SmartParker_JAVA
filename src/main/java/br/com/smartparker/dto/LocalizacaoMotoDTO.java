package br.com.smartparker.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizacaoMotoDTO {
    private Long id;
    private LocalDateTime dataAtualizada;
    private MotoDTO moto;
    private SetorDTO setor;
}