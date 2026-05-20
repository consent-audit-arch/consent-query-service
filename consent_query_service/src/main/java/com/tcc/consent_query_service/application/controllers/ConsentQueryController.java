package com.tcc.consent_query_service.application.controllers;

import com.tcc.consent_query_service.application.controllers.DTOs.requests.batch.BatchAuthorizationRequest;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.BatchAuthorizationResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentAuthorizationDetailResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentHistoryResponse;
import com.tcc.consent_query_service.application.controllers.DTOs.responses.ConsentResponse;
import com.tcc.consent_query_service.application.services.ConsentHistoryQueryService;
import com.tcc.consent_query_service.application.services.ConsentQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${base-url}")
public class ConsentQueryController {

    private final ConsentQueryService consentQueryService;
    private final ConsentHistoryQueryService consentHistoryQueryService;

    @GetMapping("/{userId}")
    public ResponseEntity<ConsentResponse> getConsentsByUserId(@PathVariable Long userId) {
        log.info("Fetching all consents for user: {}", userId);
        ConsentResponse response = consentQueryService.getConsentsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/authorizations")
    public ResponseEntity<ConsentAuthorizationDetailResponse> getConsentAuthorization(
            @PathVariable Long userId,
            @RequestParam String dataCategory,
            @RequestParam String purpose) {
        log.info("Fetching consent authorization for user: {}, dataCategory: {}, purpose: {}", 
                userId, dataCategory, purpose);
        ConsentAuthorizationDetailResponse response = consentQueryService.getConsentAuthorization(
                userId, dataCategory, purpose);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<ConsentHistoryResponse> getConsentHistory(@PathVariable Long userId) {
        log.info("Fetching consent history for user: {}", userId);
        ConsentHistoryResponse response = consentHistoryQueryService.getHistoryByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch/authorizations")
    public ResponseEntity<BatchAuthorizationResponse> checkBatchAuthorizations(
            @Valid @RequestBody BatchAuthorizationRequest request) {
        log.info("Batch authorization check for {} users, category: {}, purpose: {}",
                request.getTitularIds().size(), request.getDataCategory(), request.getPurpose());
        BatchAuthorizationResponse response = consentQueryService.checkBatchAuthorization(request);
        return ResponseEntity.ok(response);
    }
}
