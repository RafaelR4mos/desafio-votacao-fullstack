package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.agenda.AgendaCreateDTO;
import com.dbserver.votacao.dto.v1.agenda.AgendaDTO;
import com.dbserver.votacao.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaDTO> create(@Valid @RequestBody AgendaCreateDTO agendaCreateDTO) {
        return new ResponseEntity<>(agendaService.create(agendaCreateDTO), HttpStatus.CREATED);
    }
}
