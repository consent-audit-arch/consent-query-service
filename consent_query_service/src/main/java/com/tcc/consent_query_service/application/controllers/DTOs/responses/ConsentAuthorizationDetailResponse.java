package com.tcc.consent_query_service.application.controllers.DTOs.responses;

import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;

import java.time.LocalDateTime;

public record ConsentAuthorizationDetailResponse(
        Long userId,
        DataCategory dataCategory,
        Purpose purpose,
        ConsentStatus status,
        String legalBasis,
        LocalDateTime grantedAt,
        LocalDateTime revokedAt
) {
}
