package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Agenda;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    Optional<Agenda> findBySlug(String slug);

    @Query("SELECT a FROM Agenda a WHERE a.session.status = :sessionStatus")
    List<Agenda> findAllBySessionStatus(SessionStatus sessionStatus);
}
