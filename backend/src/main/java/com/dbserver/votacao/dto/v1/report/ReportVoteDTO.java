package com.dbserver.votacao.dto.v1.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportVoteDTO {

    private long totalAgenda;
    private long activeSessions;
    private long activeAssociates;
}
