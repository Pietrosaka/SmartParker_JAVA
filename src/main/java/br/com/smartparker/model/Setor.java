package br.com.smartparker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatório.")
    @Size(max = 30, message = "O nome deve ter até 30 caracteres.")
    private String nome;

    @NotNull(message = "Campo obrigatório.")
    @Positive(message = "A fileira deve ser maior ou igual a 1.")
    private int fileira;

    @NotNull(message = "Campo obrigatório.")
    @Positive(message = "A vaga deve ser maior ou igual a 1.")
    private int vaga;

    @NotNull(message = "O pátio é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "patio_id")
    private Patio patio;
}
