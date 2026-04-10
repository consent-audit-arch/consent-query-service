package com.tcc.consent_query_service.model.consent.repositories;

import com.tcc.consent_query_service.model.consent.entities.ConsentHistory;

import java.util.Optional;

public interface ConsentHistoryRepository {
    Optional<ConsentHistory> findByUserId(Long userId);
}