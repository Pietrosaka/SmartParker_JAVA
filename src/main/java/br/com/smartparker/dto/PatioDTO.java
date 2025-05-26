package br.com.smartparker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatioDTO {
    private Long id;
    private String nome;
    private String localizacao;
}