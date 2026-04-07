package com.tcc.consent_query_service.model.consent.repositories;

import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;

import java.util.List;
import java.util.Optional;

public interface ConsentViewRepository {
    List<ConsentView> findAllByUserId(Long userId);
    Optional<ConsentView> findByUserIdAndDataCategoryAndPurpose(Long userId, DataCategory dataCategory, Purpose purpose);
}
