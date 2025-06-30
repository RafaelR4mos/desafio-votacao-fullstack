package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.v1.report.ReportVoteDTO;
import com.dbserver.votacao.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportVoteDTO> reportVote() {
        return new ResponseEntity<>(reportService.reportVote(), HttpStatus.OK);
    }
}
