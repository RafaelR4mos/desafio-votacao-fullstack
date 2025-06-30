package com.dbserver.votacao.dto.v1.agenda;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaCreateDTO {

    @NotNull(message = "O título da pauta é obrigatório")
    @NotEmpty(message = "O título da pauta não pode ser vazio")
    @Size(max = 150, message = "O título da pauta deve ter no máximo 150 caracteres")
    private String title;

    @NotNull(message = "A descrição é obrigatória")
    @NotEmpty(message = "A descrição não pode ser vazia")
    @Size(min = 20, max = 255, message = "A descrição deve ter entre 20 e 255 caracteres")
    private String description;

    @NotNull(message = "A duração da sessao é obrigatória")
    @Positive(message = "a duração deve ser positiva e maior que zero")
    @Max(value = 86400, message = "A duração máxima permitida é de 24 horas")
    private int durationInSeconds;
}
