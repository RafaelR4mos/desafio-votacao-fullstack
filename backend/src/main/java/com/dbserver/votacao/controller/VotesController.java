package com.dbserver.votacao.controller;

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
}
