package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.agenda.AgendaCreateDTO;
import com.dbserver.votacao.dto.v1.agenda.AgendaDTO;
import com.dbserver.votacao.entity.enumeration.SessionStatus;
import com.dbserver.votacao.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaDTO> create(@Valid @RequestBody AgendaCreateDTO agendaCreateDTO) {
        return new ResponseEntity<>(agendaService.create(agendaCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AgendaDTO>> findAll() {
        return new ResponseEntity<>(agendaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<AgendaDTO> findBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(agendaService.findBySlug(slug), HttpStatus.OK);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<AgendaDTO>> findAllByStatus(@RequestParam(name = "s", defaultValue = "DRAFT") SessionStatus status) {
        return new ResponseEntity<>(agendaService.findAllBySessionStatus(status), HttpStatus.OK);
    }
}
