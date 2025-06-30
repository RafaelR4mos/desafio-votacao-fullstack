package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.report.ReportVoteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AgendaService agendaService;
    private final AssociateService associateService;
    private final SessionService sessionService;

    public ReportVoteDTO reportVote() {
        return new ReportVoteDTO(
                agendaService.countTotalAgendas(),
                sessionService.countAllActiveSessions(),
                associateService.countAllActiveAssociates().getTotalAssociates()
        );
    }
}
