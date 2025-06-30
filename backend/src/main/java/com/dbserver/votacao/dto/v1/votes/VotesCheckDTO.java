package com.dbserver.votacao.dto.v1.votes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VotesCheckDTO {

    private String message;
    private boolean isAlreadyVoted;
    private LocalDateTime votedAt;

    public VotesCheckDTO(String message, boolean isAlreadyVoted) {
        this.message = message;
        this.isAlreadyVoted = isAlreadyVoted;
    }
}
