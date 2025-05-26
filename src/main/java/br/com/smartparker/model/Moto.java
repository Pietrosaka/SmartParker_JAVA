package br.com.smartparker.model;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
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
public class Moto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatório.")
    @Size(min = 2, max = 50, message = "O nome precisa ter entre 2 e 50 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Não são permitidos caracteres especiais no nome.")
    private String nome;

    @NotBlank(message = "Campo obrigatório.")
    @Size(min = 2, max = 30, message = "A fabricante deve ter entre 2 e 30 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Não são permitidos caracteres especiais na fabricante.")
    private String fabricante;

    @Range(min = 100, max = 1000, message = "A cilindrada deve ser entre 100 e 1000")
    private int cilindrada;

    @NotBlank(message = "Campo obrigatório.")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$", message = "Deve ser padrão de placa Mercosul.")
    @Column(unique = true)
    private String placa;

    @NotBlank(message = "Campo obrigatório.")
    @Pattern(regexp = "^(Disponível|Em uso|Reparo)$", message = "O status deve ser: Disponível, Em uso ou Reparo.")
    private String status;

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Não podem caracteres especiais.")
    @Column(unique = true)
    private String qrCode;
}
