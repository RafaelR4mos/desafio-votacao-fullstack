package com.dbserver.votacao.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionStatus {
    DRAFT("draft"),
    VOTING("voting"),
    CLOSED("closed");

    private final String status;
}
