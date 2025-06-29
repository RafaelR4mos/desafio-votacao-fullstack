package com.dbserver.votacao.dto.v1.session;

import com.dbserver.votacao.entity.enumeration.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionDTO {

    private String idSession;
    private SessionStatus status;
    private Integer durationInSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime createdAt;
}
