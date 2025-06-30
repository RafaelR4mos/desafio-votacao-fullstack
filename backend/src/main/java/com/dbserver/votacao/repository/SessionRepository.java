package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Session;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    List<Session> findByStatusAndFinishedAtBefore(SessionStatus status, LocalDateTime finishedAt);
    long countAllByStatus(SessionStatus sessionStatus);
}
