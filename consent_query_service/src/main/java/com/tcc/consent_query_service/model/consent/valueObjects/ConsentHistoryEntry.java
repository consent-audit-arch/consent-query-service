package com.tcc.consent_query_service.model.consent.valueObjects;

import java.time.Instant;

public record ConsentHistoryEntry(
        String eventType,
        String dataCategory,
        String finality,
        Long version,
        Instant occurredAt,
        IssuedBy issuedBy
) {
}