package com.dbserver.votacao.dto.v1.votes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoteResultDTO {

    private long totalVotesYes;
    private long totalVotesNo;
    private long totalVotes;
}
