package com.dbserver.votacao.dto.v1.votes;

import com.dbserver.votacao.entity.enumeration.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VotesCreateDTO {

    @NotNull(message = "É obrigatório fornecer uma opção de voto")
    private VoteType vote;
}
