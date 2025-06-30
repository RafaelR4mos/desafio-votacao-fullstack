package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.session.SessionDTO;
import com.dbserver.votacao.dto.v1.session.SessionStartDTO;
import com.dbserver.votacao.entity.Agenda;
import com.dbserver.votacao.entity.Session;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import com.dbserver.votacao.exception.BusinessRuleException;
import com.dbserver.votacao.repository.SessionRepository;
import com.dbserver.votacao.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ModelMapper mapper;

    public Session createSessionIntoAgenda(Agenda agenda, Integer durationInSeconds) {
        Session entity = new Session();
        entity.setStatus(SessionStatus.DRAFT);
        entity.setDurationInSeconds(durationInSeconds);
        entity.setAgenda(agenda);
        return entity;
    }

    public SessionDTO openSession(String idSession, SessionStartDTO sessionStartDTO) {
        Session session = getSession(idSession);

        if(!session.getStatus().equals(SessionStatus.DRAFT)) {
            throw new BusinessRuleException("Não é possível abrir uma sessão que já foi iniciada ou encerrada");
        }

        LocalDateTime now = LocalDateTime.now();

        session.setStatus(SessionStatus.VOTING);
        session.setDurationInSeconds(sessionStartDTO.getDurationInSeconds());
        session.setStartedAt(now);
        session.setFinishedAt(now.plusSeconds(session.getDurationInSeconds()));

        Session entitySaved = sessionRepository.save(session);

        SessionDTO sessionDTO = mapper.map(entitySaved, SessionDTO.class);
        sessionDTO.setCreatedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getCreatedAt()));

        return sessionDTO;
    }

    public long countAllActiveSessions() {
        return sessionRepository.countAllByStatus(SessionStatus.VOTING);
    }

    public Session getSession(String idSession) {
        return sessionRepository.findById(idSession)
                .orElseThrow(() -> new BusinessRuleException("Nenhuma sessão de id " + idSession + " encontrada"));
    }

    public void validateSessionIsOpenToVotes(Session session) {
        if (LocalDateTime.now().isAfter(session.getFinishedAt())) {
            session.setStatus(SessionStatus.CLOSED);
            sessionRepository.save(session);
            throw new IllegalStateException("A sessão já foi encerrada. Lamentamos, mas não podemos contabilizar seu voto.");
        }
    }
}
