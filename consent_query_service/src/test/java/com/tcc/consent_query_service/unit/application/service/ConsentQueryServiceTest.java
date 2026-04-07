package com.tcc.consent_query_service.unit.application.service;

import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentAuthorizationDetailResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentResponse;
import com.tcc.consent_query_service.application.services.ConsentQueryService;
import com.tcc.consent_query_service.infrastructure.exceptions.ConsentAuthorizationNotFoundException;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
import com.tcc.consent_query_service.model.consent.repositories.ConsentViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsentQueryServiceTest {

    @Mock
    private ConsentViewRepository consentViewRepository;

    @InjectMocks
    private ConsentQueryService consentQueryService;

    private ConsentView grantedConsent;
    private ConsentView revokedConsent;

    @BeforeEach
    void setUp() {
        grantedConsent = new ConsentView(
                1L, 100L, DataCategory.PERSONAL_DATA, Purpose.PROMOTION,
                ConsentStatus.GRANTED, "CONSENT",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null, 1L, 1L, LocalDateTime.now()
        );

        revokedConsent = new ConsentView(
                2L, 100L, DataCategory.CONTRACT_DATA, Purpose.BILLING,
                ConsentStatus.REVOKED, "CONSENT",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 2, 1, 10, 0),
                2L, 2L, LocalDateTime.now()
        );
    }

    @Test
    void getConsentsByUserId_shouldReturnAllConsentsForUser() {
        Long userId = 100L;
        List<ConsentView> consents = List.of(grantedConsent, revokedConsent);
        when(consentViewRepository.findAllByUserId(userId)).thenReturn(consents);

        ConsentResponse response = consentQueryService.getConsentsByUserId(userId);

        verify(consentViewRepository).findAllByUserId(userId);
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.authorizations()).hasSize(2);
        assertThat(response.authorizations().get(0).dataCategory()).isEqualTo(DataCategory.PERSONAL_DATA);
        assertThat(response.authorizations().get(0).status()).isEqualTo(ConsentStatus.GRANTED);
        assertThat(response.authorizations().get(1).dataCategory()).isEqualTo(DataCategory.CONTRACT_DATA);
        assertThat(response.authorizations().get(1).status()).isEqualTo(ConsentStatus.REVOKED);
    }

    @Test
    void getConsentsByUserId_shouldReturnEmptyListWhenNoConsents() {
        Long userId = 999L;
        when(consentViewRepository.findAllByUserId(userId)).thenReturn(List.of());

        ConsentResponse response = consentQueryService.getConsentsByUserId(userId);

        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.authorizations()).isEmpty();
    }

    @Test
    void getConsentAuthorization_shouldReturnAuthorizationWhenFound() {
        Long userId = 100L;
        when(consentViewRepository.findByUserIdAndDataCategoryAndPurpose(
                userId, DataCategory.PERSONAL_DATA, Purpose.PROMOTION))
                .thenReturn(Optional.of(grantedConsent));

        ConsentAuthorizationDetailResponse response = consentQueryService.getConsentAuthorization(
                userId, "PERSONAL_DATA", "PROMOTION");

        verify(consentViewRepository).findByUserIdAndDataCategoryAndPurpose(
                userId, DataCategory.PERSONAL_DATA, Purpose.PROMOTION);
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.dataCategory()).isEqualTo(DataCategory.PERSONAL_DATA);
        assertThat(response.purpose()).isEqualTo(Purpose.PROMOTION);
        assertThat(response.status()).isEqualTo(ConsentStatus.GRANTED);
        assertThat(response.legalBasis()).isEqualTo("CONSENT");
        assertThat(response.grantedAt()).isNotNull();
        assertThat(response.revokedAt()).isNull();
    }

    @Test
    void getConsentAuthorization_shouldThrowExceptionWhenNotFound() {
        Long userId = 999L;
        when(consentViewRepository.findByUserIdAndDataCategoryAndPurpose(
                userId, DataCategory.PERSONAL_DATA, Purpose.PROMOTION))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> consentQueryService.getConsentAuthorization(
                userId, "PERSONAL_DATA", "PROMOTION"))
                .isInstanceOf(ConsentAuthorizationNotFoundException.class)
                .hasMessageContaining("Consent authorization not found for user: 999");
    }

    @Test
    void getConsentAuthorization_shouldThrowExceptionForInvalidDataCategory() {
        Long userId = 100L;

        assertThatThrownBy(() -> consentQueryService.getConsentAuthorization(
                userId, "INVALID_CATEGORY", "PROMOTION"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid DataCategory");
    }

    @Test
    void getConsentAuthorization_shouldThrowExceptionForInvalidPurpose() {
        Long userId = 100L;

        assertThatThrownBy(() -> consentQueryService.getConsentAuthorization(
                userId, "PERSONAL_DATA", "INVALID_PURPOSE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid Purpose");
    }

    @Test
    void getConsentAuthorization_shouldReturnRevokedAuthorization() {
        Long userId = 100L;
        when(consentViewRepository.findByUserIdAndDataCategoryAndPurpose(
                userId, DataCategory.CONTRACT_DATA, Purpose.BILLING))
                .thenReturn(Optional.of(revokedConsent));

        ConsentAuthorizationDetailResponse response = consentQueryService.getConsentAuthorization(
                userId, "CONTRACT_DATA", "BILLING");

        assertThat(response.status()).isEqualTo(ConsentStatus.REVOKED);
        assertThat(response.grantedAt()).isNotNull();
        assertThat(response.revokedAt()).isNotNull();
    }
}
