package com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository;

import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentEventJpaRepository extends JpaRepository<ConsentEventJpaEntity, Long> {
    List<ConsentEventJpaEntity> findAllByUserIdOrderByVersionAsc(Long userId);
}