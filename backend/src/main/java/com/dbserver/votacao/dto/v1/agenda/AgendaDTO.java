package com.dbserver.votacao.dto.v1.agenda;

import com.dbserver.votacao.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AgendaDTO {

    private Integer idAgenda;
    private String title;
    private String slug;
    private String description;
    private Session session;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
