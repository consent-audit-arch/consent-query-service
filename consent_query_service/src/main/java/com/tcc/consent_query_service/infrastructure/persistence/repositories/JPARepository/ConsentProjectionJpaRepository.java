package com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository;

import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentProjectionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentProjectionJpaRepository extends JpaRepository<ConsentProjectionJpaEntity, Long> {

    List<ConsentProjectionJpaEntity> findAllByUserId(Long userId);

    Optional<ConsentProjectionJpaEntity> findByUserIdAndDataCategoryAndFinality(
            Long userId, String dataCategory, String finality);
}
