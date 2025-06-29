package com.dbserver.votacao.dto.v1.agenda;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(max = 255, message = "O slug deve ter no máximo 255 caracteres")
    private String description;
}
