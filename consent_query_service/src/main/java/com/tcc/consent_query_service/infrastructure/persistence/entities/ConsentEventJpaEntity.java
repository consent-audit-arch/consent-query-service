package com.tcc.consent_query_service.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stream_id", nullable = false, length = 100)
    private String streamId;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "data_category", nullable = false, length = 100)
    private String dataCategory;

    @Column(name = "finality", nullable = false, length = 100)
    private String finality;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "issued_by", columnDefinition = "jsonb")
    private String issuedBy;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}