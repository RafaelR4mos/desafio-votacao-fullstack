package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AssociateRepository extends JpaRepository<Associate, String> {
}
