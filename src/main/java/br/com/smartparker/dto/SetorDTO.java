package br.com.smartparker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetorDTO {
    private Long id;
    private String nome;
    private int fileira;
    private int vaga;
    private PatioDTO patio;
}