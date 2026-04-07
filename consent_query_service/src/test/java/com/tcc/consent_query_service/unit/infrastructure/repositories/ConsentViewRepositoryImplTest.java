package com.tcc.consent_query_service.unit.infrastructure.repositories;

import com.tcc.consent_query_service.infrastructure.mappers.ConsentProjectionMapper;
import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentProjectionJpaEntity;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.ConsentViewRepositoryImpl;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository.ConsentProjectionJpaRepository;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsentViewRepositoryImplTest {

    @Mock
    private ConsentProjectionJpaRepository jpaRepository;

    @Mock
    private ConsentProjectionMapper mapper;

    @InjectMocks
    private ConsentViewRepositoryImpl repository;

    private ConsentProjectionJpaEntity jpaEntity;
    private ConsentView domainEntity;

    @BeforeEach
    void setUp() {
        jpaEntity = createJpaEntity(1L, 100L, "PERSONAL_DATA", "PROMOTION", 
                "GRANTED", "CONSENT", 
                LocalDateTime.of(2026, 1, 1, 10, 0), null, 1L, 1L);

        domainEntity = new ConsentView(
                1L, 100L, DataCategory.PERSONAL_DATA, Purpose.PROMOTION,
                ConsentStatus.GRANTED, "CONSENT",
                LocalDateTime.of(2026, 1, 1, 10, 0),
                null, 1L, 1L, LocalDateTime.now()
        );
    }

    private ConsentProjectionJpaEntity createJpaEntity(Long id, Long userId, String dataCategory, 
            String finality, String status, String legalBasis, 
            LocalDateTime grantedAt, LocalDateTime revokedAt, Long lastEventId, Long version) {
        ConsentProjectionJpaEntity entity = new ConsentProjectionJpaEntity();
        try {
            var fields = ConsentProjectionJpaEntity.class.getDeclaredFields();
            for (var field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "id" -> field.set(entity, id);
                    case "userId" -> field.set(entity, userId);
                    case "dataCategory" -> field.set(entity, dataCategory);
                    case "finality" -> field.set(entity, finality);
                    case "status" -> field.set(entity, status);
                    case "legalBasis" -> field.set(entity, legalBasis);
                    case "grantedAt" -> field.set(entity, grantedAt);
                    case "revokedAt" -> field.set(entity, revokedAt);
                    case "lastEventId" -> field.set(entity, lastEventId);
                    case "version" -> field.set(entity, version);
                    case "updatedAt" -> field.set(entity, LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Test
    void findAllByUserId_shouldReturnMappedEntities() {
        Long userId = 100L;
        List<ConsentProjectionJpaEntity> jpaEntities = List.of(jpaEntity);
        List<ConsentView> domainEntities = List.of(domainEntity);

        when(jpaRepository.findAllByUserId(userId)).thenReturn(jpaEntities);
        when(mapper.toDomainList(jpaEntities)).thenReturn(domainEntities);

        List<ConsentView> result = repository.findAllByUserId(userId);

        verify(jpaRepository).findAllByUserId(userId);
        verify(mapper).toDomainList(jpaEntities);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(userId);
    }

    @Test
    void findAllByUserId_shouldReturnEmptyListWhenNoResults() {
        Long userId = 999L;
        when(jpaRepository.findAllByUserId(userId)).thenReturn(List.of());
        when(mapper.toDomainList(List.of())).thenReturn(List.of());

        List<ConsentView> result = repository.findAllByUserId(userId);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUserIdAndDataCategoryAndPurpose_shouldReturnMappedEntity() {
        Long userId = 100L;
        DataCategory dataCategory = DataCategory.PERSONAL_DATA;
        Purpose purpose = Purpose.PROMOTION;

        when(jpaRepository.findByUserIdAndDataCategoryAndFinality(
                userId, dataCategory.name(), purpose.name()))
                .thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(domainEntity);

        Optional<ConsentView> result = repository.findByUserIdAndDataCategoryAndPurpose(
                userId, dataCategory, purpose);

        verify(jpaRepository).findByUserIdAndDataCategoryAndFinality(
                userId, dataCategory.name(), purpose.name());
        verify(mapper).toDomain(jpaEntity);
        assertThat(result).isPresent();
        assertThat(result.get().dataCategory()).isEqualTo(DataCategory.PERSONAL_DATA);
        assertThat(result.get().purpose()).isEqualTo(Purpose.PROMOTION);
    }

    @Test
    void findByUserIdAndDataCategoryAndPurpose_shouldReturnEmptyWhenNotFound() {
        Long userId = 999L;
        DataCategory dataCategory = DataCategory.PERSONAL_DATA;
        Purpose purpose = Purpose.PROMOTION;

        when(jpaRepository.findByUserIdAndDataCategoryAndFinality(
                userId, dataCategory.name(), purpose.name()))
                .thenReturn(Optional.empty());

        Optional<ConsentView> result = repository.findByUserIdAndDataCategoryAndPurpose(
                userId, dataCategory, purpose);

        assertThat(result).isEmpty();
    }
}
