package com.tcc.consent_query_service.model.consent.entities;

import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;

import java.time.LocalDateTime;

public record ConsentView(
        Long id,
        Long userId,
        DataCategory dataCategory,
        Purpose purpose,
        ConsentStatus status,
        String legalBasis,
        LocalDateTime grantedAt,
        LocalDateTime revokedAt,
        Long lastEventId,
        Long version,
        LocalDateTime updatedAt
) {
}
