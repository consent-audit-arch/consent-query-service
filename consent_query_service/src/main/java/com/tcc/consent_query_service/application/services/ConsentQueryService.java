package com.tcc.consent_query_service.application.services;

import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentAuthorizationDetailResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentResponse;
import com.tcc.consent_query_service.infrastructure.exceptions.ConsentAuthorizationNotFoundException;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
import com.tcc.consent_query_service.model.consent.repositories.ConsentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsentQueryService {

    private final ConsentViewRepository consentViewRepository;

    public ConsentResponse getConsentsByUserId(Long userId) {
        List<ConsentView> consents = consentViewRepository.findAllByUserId(userId);

        List<ConsentResponse.AuthorizationDetail> authorizations = consents.stream()
                .map(consent -> new ConsentResponse.AuthorizationDetail(
                        consent.dataCategory(),
                        consent.purpose(),
                        consent.status(),
                        consent.legalBasis(),
                        consent.grantedAt(),
                        consent.revokedAt()
                ))
                .toList();

        return new ConsentResponse(userId, authorizations);
    }

    public ConsentAuthorizationDetailResponse getConsentAuthorization(
            Long userId,
            String dataCategoryStr,
            String purposeStr) {

        DataCategory dataCategory = DataCategory.of(dataCategoryStr);
        Purpose purpose = Purpose.of(purposeStr);

        ConsentView consent = consentViewRepository
                .findByUserIdAndDataCategoryAndPurpose(userId, dataCategory, purpose)
                .orElseThrow(() -> new ConsentAuthorizationNotFoundException(userId, dataCategory, purpose));

        return new ConsentAuthorizationDetailResponse(
                consent.userId(),
                consent.dataCategory(),
                consent.purpose(),
                consent.status(),
                consent.legalBasis(),
                consent.grantedAt(),
                consent.revokedAt()
        );
    }
}
