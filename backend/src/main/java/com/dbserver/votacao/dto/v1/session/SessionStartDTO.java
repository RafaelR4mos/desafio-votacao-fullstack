package com.dbserver.votacao.dto.v1.session;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionStartDTO {

    @NotNull(message = "A duração da sessao é obrigatória")
    @Positive(message = "a duração deve ser positiva e maior que zero")
    @Max(value = 86400, message = "A duração máxima permitida é de 24 horas")
    private int durationInSeconds;
}
