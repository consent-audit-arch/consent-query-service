package com.tcc.consent_query_service.unit.application.service;

import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentHistoryEntryResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentHistoryResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.IssuedByResponse;
import com.tcc.consent_query_service.application.services.ConsentHistoryQueryService;
import com.tcc.consent_query_service.infrastructure.exceptions.ConsentHistoryNotFoundException;
import com.tcc.consent_query_service.model.consent.entities.ConsentHistory;
import com.tcc.consent_query_service.model.consent.repositories.ConsentHistoryRepository;
import com.tcc.consent_query_service.model.consent.valueObjects.ConsentHistoryEntry;
import com.tcc.consent_query_service.model.consent.valueObjects.IssuedBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsentHistoryQueryServiceTest {

    @Mock
    private ConsentHistoryRepository consentHistoryRepository;

    @InjectMocks
    private ConsentHistoryQueryService consentHistoryQueryService;

    @Test
    void getHistoryByUserId_shouldReturnHistoryWhenFound() {
        Long userId = 100L;
        IssuedBy issuedBy = new IssuedBy("system-001", "SYSTEM");
        ConsentHistoryEntry entry1 = new ConsentHistoryEntry(
                "ConsentGranted",
                "PERSONAL_DATA",
                "PROMOTION",
                1L,
                Instant.parse("2026-01-01T10:00:00Z"),
                issuedBy
        );
        ConsentHistoryEntry entry2 = new ConsentHistoryEntry(
                "ConsentRevoked",
                "PERSONAL_DATA",
                "PROMOTION",
                2L,
                Instant.parse("2026-02-01T10:00:00Z"),
                issuedBy
        );
        ConsentHistory history = new ConsentHistory(userId, List.of(entry1, entry2));

        when(consentHistoryRepository.findByUserId(userId)).thenReturn(Optional.of(history));

        ConsentHistoryResponse response = consentHistoryQueryService.getHistoryByUserId(userId);

        verify(consentHistoryRepository).findByUserId(userId);
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.events()).hasSize(2);

        ConsentHistoryEntryResponse firstEvent = response.events().get(0);
        assertThat(firstEvent.eventType()).isEqualTo("ConsentGranted");
        assertThat(firstEvent.dataCategory()).isEqualTo("PERSONAL_DATA");
        assertThat(firstEvent.finality()).isEqualTo("PROMOTION");
        assertThat(firstEvent.version()).isEqualTo(1L);
        assertThat(firstEvent.issuedBy()).isEqualTo(new IssuedByResponse("system-001", "SYSTEM"));

        ConsentHistoryEntryResponse secondEvent = response.events().get(1);
        assertThat(secondEvent.eventType()).isEqualTo("ConsentRevoked");
        assertThat(secondEvent.version()).isEqualTo(2L);
    }

    @Test
    void getHistoryByUserId_shouldReturnHistoryWithNullIssuedBy() {
        Long userId = 100L;
        ConsentHistoryEntry entry = new ConsentHistoryEntry(
                "ConsentGranted",
                "PERSONAL_DATA",
                "PROMOTION",
                1L,
                Instant.parse("2026-01-01T10:00:00Z"),
                null
        );
        ConsentHistory history = new ConsentHistory(userId, List.of(entry));

        when(consentHistoryRepository.findByUserId(userId)).thenReturn(Optional.of(history));

        ConsentHistoryResponse response = consentHistoryQueryService.getHistoryByUserId(userId);

        assertThat(response.events().get(0).issuedBy()).isNull();
    }

    @Test
    void getHistoryByUserId_shouldThrowExceptionWhenNotFound() {
        Long userId = 999L;
        when(consentHistoryRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consentHistoryQueryService.getHistoryByUserId(userId))
                .isInstanceOf(ConsentHistoryNotFoundException.class)
                .hasMessageContaining("Consent history not found for user: 999");
    }

    @Test
    void getHistoryByUserId_shouldReturnEmptyListWhenHistoryHasNoEvents() {
        Long userId = 100L;
        ConsentHistory history = new ConsentHistory(userId, List.of());
        when(consentHistoryRepository.findByUserId(userId)).thenReturn(Optional.of(history));

        ConsentHistoryResponse response = consentHistoryQueryService.getHistoryByUserId(userId);

        assertThat(response.events()).isEmpty();
    }
}