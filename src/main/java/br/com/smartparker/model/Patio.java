package br.com.smartparker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 30, message = "Deve ter entre até 30 digitos.")
    private String nome;

    @NotBlank(message = "Campo obrigatório.")
    @Size(min = 5, max = 100, message = "Deve ter entre 5 e 100 digitos.")
    private String localizacao;
}
