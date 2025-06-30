package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.votes.VoteResultDTO;
import com.dbserver.votacao.dto.v1.votes.VotesCheckDTO;
import com.dbserver.votacao.dto.v1.votes.VotesCreateDTO;
import com.dbserver.votacao.dto.v1.votes.VotesDTO;
import com.dbserver.votacao.service.VotesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VotesController {

    private final VotesService votesService;

    @PostMapping("/session/{idSession}/associate/{idAssociate}/vote")
    public ResponseEntity<VotesDTO> vote(@PathVariable String idSession,
                                         @PathVariable String idAssociate,
                                         @Valid @RequestBody VotesCreateDTO votesCreateDTO) {
        return new ResponseEntity<>(votesService.vote(idSession, idAssociate, votesCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/session/{idSession}/associate/{idAssociate}/check")
    public ResponseEntity<VotesCheckDTO> checkVote(@PathVariable String idSession,
                                                   @PathVariable String idAssociate) {
        return new ResponseEntity<>(votesService.checkIfAssociateAlreadyVotedInSession(idSession, idAssociate), HttpStatus.OK);
    }

    @GetMapping("/session/{idSession}/results")
    public ResponseEntity<VoteResultDTO> voteResults(@PathVariable String idSession) {
        return new ResponseEntity<>(votesService.getVoteResult(idSession), HttpStatus.OK);
    }
}
