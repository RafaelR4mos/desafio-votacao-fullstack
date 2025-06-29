package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.session.SessionDTO;
import com.dbserver.votacao.dto.v1.session.SessionStartDTO;
import com.dbserver.votacao.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PutMapping("/{idSession}/start")
    public ResponseEntity<SessionDTO> startSession(@Valid @RequestBody SessionStartDTO sessionStartDTO,
                                                    @PathVariable String idSession) {
        return new ResponseEntity<>(sessionService.openSession(idSession, sessionStartDTO), HttpStatus.OK);
    }
}
