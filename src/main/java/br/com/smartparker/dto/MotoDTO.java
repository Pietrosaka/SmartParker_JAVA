package br.com.smartparker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotoDTO {
    private Long id;
    private String nome;
    private String fabricante;
    private int cilindrada;
    private String placa;
    private String status;
    private String qrCode;
}