package com.tcc.consent_query_service.application.controllers.DTOs.responses;

public record TitularAuthorizationResult(
        Long titularId,
        boolean authorized,
        String reason
) {
}
