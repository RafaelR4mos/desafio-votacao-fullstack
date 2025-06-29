package com.dbserver.votacao.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VoteId implements Serializable {

    @Column(name = "id_session")
    private String idSession;

    @Column(name = "id_associate")
    private String idAssociate;
}
