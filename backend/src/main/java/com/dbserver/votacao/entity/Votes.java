package com.dbserver.votacao.entity;

import com.dbserver.votacao.entity.enumeration.VoteType;
import com.dbserver.votacao.entity.pk.VoteId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Table(name = "votes")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Votes {
    @EmbeddedId
    private VoteId voteId;

    @Column(name = "vote")
    private VoteType vote;

    @CreationTimestamp
    @Column(name = "voted_at")
    private Timestamp votedAt;
}
