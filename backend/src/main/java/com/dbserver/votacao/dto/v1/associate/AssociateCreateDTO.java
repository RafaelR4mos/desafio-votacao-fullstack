package com.dbserver.votacao.dto.v1.associate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssociateCreateDTO {

    @NotNull(message = "O primeiro nome é obrigatório")
    @NotEmpty(message = "O primeiro nome não pode ser vazio")
    private String firstName;

    @NotNull(message = "O último nome é obrigatório")
    @NotEmpty(message = "O último nome não pode ser vazio")
    private String lastName;

    @NotNull(message = "O CPF é obrigatório")
    @CPF(message = "O CPF deve ser válido")
    private String cpf;

    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String email;
}
