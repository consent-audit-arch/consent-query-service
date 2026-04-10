package com.tcc.consent_query_service.application.controllers.DTOs.responses;

import java.util.List;

public record ConsentHistoryResponse(Long userId, List<ConsentHistoryEntryResponse> events) {
}