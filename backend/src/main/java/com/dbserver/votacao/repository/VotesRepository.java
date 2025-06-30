package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Votes;
import com.dbserver.votacao.entity.enumeration.VoteType;
import com.dbserver.votacao.entity.pk.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, VoteId> {
    boolean existsById(VoteId voteId);

    boolean existsByVoteId_IdSessionAndVoteId_IdAssociate(String idSession, String idAssociate);

    /**
     * @param idSession
     * @param idAssociate
     * @return
     */
    Votes findByVoteId_IdSessionAndVoteId_IdAssociate(String idSession, String idAssociate);

    long countByVoteId_IdSession(String session);
    long countByVoteId_IdSessionAndVote(String idSession, VoteType vote);
}
