package br.com.smartparker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "Formato de email inválido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF precisa ter exatamente 11 números (sem caracteres especiais).")
    @Column(unique = true)
    private String cpf;

    @OneToOne
    private Moto moto;
}
