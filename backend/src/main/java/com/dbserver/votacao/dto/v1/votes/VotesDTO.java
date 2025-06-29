package com.dbserver.votacao.dto.v1.votes;


import com.dbserver.votacao.entity.enumeration.VoteType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VotesDTO {

    private String idSession;
    private String idAssociate;
    private VoteType vote;
    private LocalDateTime votedAt;
}
