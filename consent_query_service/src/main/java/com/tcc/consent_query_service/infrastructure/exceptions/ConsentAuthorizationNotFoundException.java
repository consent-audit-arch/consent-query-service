package com.tcc.consent_query_service.infrastructure.exceptions;

import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;

public class ConsentAuthorizationNotFoundException extends RuntimeException {

    private final Long userId;
    private final DataCategory dataCategory;
    private final Purpose purpose;

    public ConsentAuthorizationNotFoundException(Long userId, DataCategory dataCategory, Purpose purpose) {
        super(String.format("Consent authorization not found for user: %d, dataCategory: %s, purpose: %s",
                userId, dataCategory, purpose));
        this.userId = userId;
        this.dataCategory = dataCategory;
        this.purpose = purpose;
    }

    public Long getUserId() {
        return userId;
    }

    public DataCategory getDataCategory() {
        return dataCategory;
    }

    public Purpose getPurpose() {
        return purpose;
    }
}
