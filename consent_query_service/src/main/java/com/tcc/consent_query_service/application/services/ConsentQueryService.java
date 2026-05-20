package com.tcc.consent_query_service.application.services;

import com.tcc.consent_query_service.application.controllers.DTOs.requests.batch.BatchAuthorizationRequest;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.BatchAuthorizationResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentAuthorizationDetailResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.TitularAuthorizationResult;
import com.tcc.consent_query_service.infrastructure.exceptions.ConsentAuthorizationNotFoundException;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
import com.tcc.consent_query_service.model.consent.repositories.ConsentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TitularAuthorizationResult checkAuthorization(
            Long titularId, DataCategory dataCategory, Purpose purpose) {

        Optional<ConsentView> consentOpt = consentViewRepository
                .findByUserIdAndDataCategoryAndPurpose(titularId, dataCategory, purpose);

        if (consentOpt.isEmpty()) {
            return new TitularAuthorizationResult(titularId, false, "CONSENT_NOT_FOUND");
        }

        ConsentView consent = consentOpt.get();

        if (consent.status() == ConsentStatus.REVOKED) {
            return new TitularAuthorizationResult(titularId, false, "CONSENT_REVOKED");
        }

        return new TitularAuthorizationResult(titularId, true, null);
    }

    public BatchAuthorizationResponse checkBatchAuthorization(BatchAuthorizationRequest request) {
        DataCategory dataCategory = DataCategory.of(request.getDataCategory());
        Purpose purpose = Purpose.of(request.getPurpose());

        Map<Long, ConsentView> consentMap = consentViewRepository
                .findAllByUserIdIn(request.getTitularIds())
                .stream()
                .filter(cv -> cv.dataCategory() == dataCategory && cv.purpose() == purpose)
                .collect(Collectors.toMap(ConsentView::userId, cv -> cv, (a, b) -> a));

        List<TitularAuthorizationResult> results = new ArrayList<>();

        for (Long titularId : request.getTitularIds()) {
            ConsentView consent = consentMap.get(titularId);

            if (consent == null) {
                results.add(new TitularAuthorizationResult(titularId, false, "CONSENT_NOT_FOUND"));
            } else if (consent.status() == ConsentStatus.REVOKED) {
                results.add(new TitularAuthorizationResult(titularId, false, "CONSENT_REVOKED"));
            } else {
                results.add(new TitularAuthorizationResult(titularId, true, null));
            }
        }

        return new BatchAuthorizationResponse(results);
    }
}
