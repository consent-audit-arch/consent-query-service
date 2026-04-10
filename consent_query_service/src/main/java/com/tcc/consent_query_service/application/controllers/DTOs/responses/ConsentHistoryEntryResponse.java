package com.tcc.consent_query_service.application.controllers.DTOs.responses;

import java.time.Instant;

public record ConsentHistoryEntryResponse(
        String eventType,
        String dataCategory,
        String finality,
        Long version,
        Instant occurredAt,
        IssuedByResponse issuedBy
) {
}