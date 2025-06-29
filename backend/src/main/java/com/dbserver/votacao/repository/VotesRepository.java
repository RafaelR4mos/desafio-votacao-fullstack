package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Votes;
import com.dbserver.votacao.entity.pk.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, VoteId> {
    boolean existsById(VoteId voteId);
}
