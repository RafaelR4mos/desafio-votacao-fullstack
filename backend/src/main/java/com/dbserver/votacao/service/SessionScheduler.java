package com.dbserver.votacao.service;

import com.dbserver.votacao.entity.Session;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import com.dbserver.votacao.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionScheduler {

    private final SessionRepository sessionRepository;

    @Scheduled(fixedRate = 60000) // a cada 1 minuto
    public void finishExpiredSessions() {
        List<Session> expirar = sessionRepository.findByStatusAndFinishedAtBefore(
                SessionStatus.VOTING, LocalDateTime.now()
        );

        for (Session session : expirar) {
            session.setStatus(SessionStatus.CLOSED);
            log.info("Sessão {} encerrada!", session.getIdSession());
        }

        sessionRepository.saveAll(expirar);
    }
}
