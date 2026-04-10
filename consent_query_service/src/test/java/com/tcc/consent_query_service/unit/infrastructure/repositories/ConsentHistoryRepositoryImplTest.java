package com.tcc.consent_query_service.unit.infrastructure.repositories;

import com.tcc.consent_query_service.infrastructure.mappers.ConsentEventMapper;
import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentEventJpaEntity;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository.ConsentEventJpaRepository;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.ConsentHistoryRepositoryImpl;
import com.tcc.consent_query_service.model.consent.entities.ConsentHistory;
import com.tcc.consent_query_service.model.consent.valueObjects.ConsentHistoryEntry;
import com.tcc.consent_query_service.model.consent.valueObjects.IssuedBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsentHistoryRepositoryImplTest {

    @Mock
    private ConsentEventJpaRepository jpaRepository;

    @Mock
    private ConsentEventMapper mapper;

    @Test
    void findByUserId_shouldReturnHistoryWhenEventsExist() throws Exception {
        ConsentHistoryRepositoryImpl repository = new ConsentHistoryRepositoryImpl(jpaRepository, mapper);
        Long userId = 100L;
        IssuedBy issuedBy = new IssuedBy("system-001", "USER");
        ConsentHistoryEntry entry = new ConsentHistoryEntry(
                "ConsentGranted", "PERSONAL_DATA", "PROMOTION",
                1L, Instant.parse("2026-01-01T10:00:00Z"), issuedBy
        );
        ConsentEventJpaEntity jpaEntity = new ConsentEventJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setUserId(userId);

        when(jpaRepository.findAllByUserIdOrderByVersionAsc(userId)).thenReturn(List.of(jpaEntity));
        when(mapper.toDomainList(anyList())).thenReturn(List.of(entry));

        Optional<ConsentHistory> result = repository.findByUserId(userId);

        assertThat(result).isPresent();
        assertThat(result.get().userId()).isEqualTo(userId);
        assertThat(result.get().events()).hasSize(1);
    }

    @Test
    void findByUserId_shouldReturnEmptyWhenNoEvents() throws Exception {
        ConsentHistoryRepositoryImpl repository = new ConsentHistoryRepositoryImpl(jpaRepository, mapper);
        Long userId = 999L;

        when(jpaRepository.findAllByUserIdOrderByVersionAsc(userId)).thenReturn(Collections.emptyList());

        Optional<ConsentHistory> result = repository.findByUserId(userId);

        assertThat(result).isEmpty();
    }
}