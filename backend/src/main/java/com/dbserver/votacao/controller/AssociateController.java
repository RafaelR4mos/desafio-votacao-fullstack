package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.associate.AssociateCreateDTO;
import com.dbserver.votacao.dto.v1.associate.AssociateDTO;
import com.dbserver.votacao.dto.v1.associate.AssociateStatsDTO;
import com.dbserver.votacao.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/associate")
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService associateService;

    @PostMapping
    public ResponseEntity<AssociateDTO> create(@Valid @RequestBody AssociateCreateDTO associateCreateDTO) {
        return new ResponseEntity<>(associateService.create(associateCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/by-cpf/{cpf}")
    public ResponseEntity<AssociateDTO> findByCpf(@PathVariable String cpf) {
        return new ResponseEntity<>(associateService.findByCpf(cpf), HttpStatus.OK);
    }

    @GetMapping("/count-all")
    public ResponseEntity<AssociateStatsDTO> countAll() {
        return new ResponseEntity<>(associateService.countAllActiveAssociates(), HttpStatus.OK);
    }
}
