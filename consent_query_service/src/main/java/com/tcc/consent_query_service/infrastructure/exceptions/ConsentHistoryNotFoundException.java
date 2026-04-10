package com.tcc.consent_query_service.infrastructure.exceptions;

public class ConsentHistoryNotFoundException extends RuntimeException {

    public ConsentHistoryNotFoundException(Long userId) {
        super("Consent history not found for user: " + userId);
    }
}