package com.tcc.consent_query_service.model.consent.valueObjects;

import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;

import java.util.Set;

public record ConsentViewSummary(
        Long userId,
        Set<ConsentAuthorizationView> authorizations
) {
    public record ConsentAuthorizationView(
            DataCategory dataCategory,
            ConsentStatus status
    ) {
    }
}
