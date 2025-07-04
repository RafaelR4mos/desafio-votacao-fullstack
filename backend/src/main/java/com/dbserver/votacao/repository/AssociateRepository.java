package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AssociateRepository extends JpaRepository<Associate, String> {

    long countAllByActive(boolean active);
    Optional<Associate> findByCpf(String cpf);
}
