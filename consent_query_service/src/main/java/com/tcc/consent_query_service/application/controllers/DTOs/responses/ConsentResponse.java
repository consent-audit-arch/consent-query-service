package com.tcc.consent_query_service.application.controllers.DTOs.responses;

import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;

import java.time.LocalDateTime;
import java.util.List;

public record ConsentResponse(
        Long userId,
        List<AuthorizationDetail> authorizations
) {
    public record AuthorizationDetail(
            DataCategory dataCategory,
            Purpose purpose,
            ConsentStatus status,
            String legalBasis,
            LocalDateTime grantedAt,
            LocalDateTime revokedAt
    ) {
    }
}
