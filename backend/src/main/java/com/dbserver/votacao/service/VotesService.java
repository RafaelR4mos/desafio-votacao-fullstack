package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.v1.votes.VoteResultDTO;
import com.dbserver.votacao.dto.v1.votes.VotesCheckDTO;
import com.dbserver.votacao.dto.v1.votes.VotesCreateDTO;
import com.dbserver.votacao.dto.v1.votes.VotesDTO;
import com.dbserver.votacao.entity.Associate;
import com.dbserver.votacao.entity.Session;
import com.dbserver.votacao.entity.Votes;
import com.dbserver.votacao.entity.enumeration.VoteType;
import com.dbserver.votacao.entity.pk.VoteId;
import com.dbserver.votacao.exception.BusinessRuleException;
import com.dbserver.votacao.repository.VotesRepository;
import com.dbserver.votacao.util.DateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotesService {

    private final VotesRepository votesRepository;
    private final AssociateService associateService;
    private final SessionService sessionService;

    public VotesDTO vote(String idSession, String idAssociate, VotesCreateDTO votesDTO) {
        Associate associate = associateService.getAssociate(idAssociate);
        Session session = sessionService.getSession(idSession);

        sessionService.validateSessionIsOpenToVotes(session);
        validateAssociateIsAbleToVote(associate.getIdAssociate(), session.getIdSession());

        Votes vote = new Votes();
        vote.setVoteId(new VoteId(session.getIdSession(), associate.getIdAssociate()));
        vote.setVote(votesDTO.getVote());

        Votes entitySaved = votesRepository.save(vote);

        return VotesDTO.builder()
                .idSession(entitySaved.getVoteId().getIdSession())
                .idAssociate(entitySaved.getVoteId().getIdAssociate())
                .vote(entitySaved.getVote())
                .votedAt(DateConverter.convertTimestampToLocalDateTime(entitySaved.getVotedAt()))
                .build();
    }

    public VotesCheckDTO checkIfAssociateAlreadyVotedInSession(
            String idSession,
            String idAssociate
    ) {
        Associate associate = associateService.getAssociate(idAssociate);
        Session session = sessionService.getSession(idSession);

        if (votesRepository.existsByVoteId_IdSessionAndVoteId_IdAssociate(session.getIdSession(), associate.getIdAssociate())) {
            Votes vote = votesRepository.findByVoteId_IdSessionAndVoteId_IdAssociate(session.getIdSession(), associate.getIdAssociate());
            return new VotesCheckDTO("O Associado já registrou seu voto nesta sessão", true, DateConverter.convertTimestampToLocalDateTime(vote.getVotedAt()));
        } else {
            return new VotesCheckDTO("O Associado ainda não registrou seu voto nesta sessão", false);
        }
    }

    public VoteResultDTO getVoteResult(String idSession) {

        return new VoteResultDTO(
                votesRepository.countByVoteId_IdSessionAndVote(idSession, VoteType.SIM),
                votesRepository.countByVoteId_IdSessionAndVote(idSession, VoteType.NAO),
                votesRepository.countByVoteId_IdSession(idSession)
        );
    }

    private void validateAssociateIsAbleToVote(String idAssociate, String idSession) {
        if (votesRepository.existsById(new VoteId(idSession, idAssociate))) {
            throw new BusinessRuleException("O Associado já registrou seu voto nesta sessão");
        }
    }
}
