package com.dbserver.votacao.controller;

import com.dbserver.votacao.entity.AssociateCreateDTO;
import com.dbserver.votacao.entity.AssociateDTO;
import com.dbserver.votacao.service.AssociateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associate")
@RequiredArgsConstructor
public class AssociateController {

    private final AssociateService associateService;

    @PostMapping
    public ResponseEntity<AssociateDTO> create(@Valid @RequestBody AssociateCreateDTO associateCreateDTO) {
        return new ResponseEntity<>(associateService.create(associateCreateDTO), HttpStatus.CREATED);
    }
}
