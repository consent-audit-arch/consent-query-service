package com.tcc.consent_query_service.infrastructure.persistence.repositories;

import com.tcc.consent_query_service.infrastructure.mappers.ConsentEventMapper;
import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentEventJpaEntity;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository.ConsentEventJpaRepository;
import com.tcc.consent_query_service.model.consent.entities.ConsentHistory;
import com.tcc.consent_query_service.model.consent.repositories.ConsentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConsentHistoryRepositoryImpl implements ConsentHistoryRepository {

    private final ConsentEventJpaRepository jpaRepository;
    private final ConsentEventMapper mapper;

    @Override
    public Optional<ConsentHistory> findByUserId(Long userId) {
        List<ConsentEventJpaEntity> entities = jpaRepository.findAllByUserIdOrderByVersionAsc(userId);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        var entries = mapper.toDomainList(entities);
        return Optional.of(new ConsentHistory(userId, entries));
    }
}