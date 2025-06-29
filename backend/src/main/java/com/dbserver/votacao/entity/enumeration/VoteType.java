package com.dbserver.votacao.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteType {
    SIM("sim"),
    NAO("não");

    private final String vote;
}
