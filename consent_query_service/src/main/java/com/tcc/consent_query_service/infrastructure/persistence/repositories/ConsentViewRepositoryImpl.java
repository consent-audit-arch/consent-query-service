package com.tcc.consent_query_service.infrastructure.persistence.repositories;

import com.tcc.consent_query_service.infrastructure.mappers.ConsentProjectionMapper;
import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentProjectionJpaEntity;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository.ConsentProjectionJpaRepository;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
import com.tcc.consent_query_service.model.consent.repositories.ConsentViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConsentViewRepositoryImpl implements ConsentViewRepository {

    private final ConsentProjectionJpaRepository jpaRepository;
    private final ConsentProjectionMapper mapper;

    @Override
    public List<ConsentView> findAllByUserId(Long userId) {
        List<ConsentProjectionJpaEntity> entities = jpaRepository.findAllByUserId(userId);
        return mapper.toDomainList(entities);
    }

    @Override
    public Optional<ConsentView> findByUserIdAndDataCategoryAndPurpose(
            Long userId, DataCategory dataCategory, Purpose purpose) {
        return jpaRepository.findByUserIdAndDataCategoryAndFinality(
                        userId, dataCategory.name(), purpose.name())
                .map(mapper::toDomain);
    }
}
