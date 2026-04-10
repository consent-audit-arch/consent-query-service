package com.tcc.consent_query_service.application.services;

import com.tcc.consent_query_service.application.controllers.DTOs.responses.*;
import com.tcc.consent_query_service.infrastructure.exceptions.ConsentHistoryNotFoundException;
import com.tcc.consent_query_service.model.consent.entities.ConsentHistory;
import com.tcc.consent_query_service.model.consent.repositories.ConsentHistoryRepository;
import com.tcc.consent_query_service.model.consent.valueObjects.ConsentHistoryEntry;
import com.tcc.consent_query_service.model.consent.valueObjects.IssuedBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentHistoryQueryService {

    private final ConsentHistoryRepository consentHistoryRepository;

    public ConsentHistoryResponse getHistoryByUserId(Long userId) {
        ConsentHistory history = consentHistoryRepository.findByUserId(userId)
                .orElseThrow(() -> new ConsentHistoryNotFoundException(userId));

        List<ConsentHistoryEntryResponse> events = history.events().stream()
                .map(this::toResponse)
                .toList();

        return new ConsentHistoryResponse(userId, events);
    }

    private ConsentHistoryEntryResponse toResponse(ConsentHistoryEntry entry) {
        IssuedBy issuedBy = entry.issuedBy();
        IssuedByResponse issuedByResponse = issuedBy != null
                ? new IssuedByResponse(issuedBy.id(), issuedBy.type())
                : null;

        return new ConsentHistoryEntryResponse(
                entry.eventType(),
                entry.dataCategory(),
                entry.finality(),
                entry.version(),
                entry.occurredAt(),
                issuedByResponse
        );
    }
}