package com.tcc.consent_query_service.infrastructure.mappers;

import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentProjectionJpaEntity;
import com.tcc.consent_query_service.model.consent.entities.ConsentView;
import com.tcc.consent_query_service.model.consent.enums.ConsentStatus;
import com.tcc.consent_query_service.model.consent.enums.DataCategory;
import com.tcc.consent_query_service.model.consent.enums.Purpose;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsentProjectionMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "dataCategory", expression = "java(DataCategory.of(entity.getDataCategory()))")
    @Mapping(target = "purpose", expression = "java(Purpose.of(entity.getFinality()))")
    @Mapping(target = "status", expression = "java(ConsentStatus.of(entity.getStatus()))")
    @Mapping(target = "legalBasis", source = "legalBasis")
    @Mapping(target = "grantedAt", source = "grantedAt")
    @Mapping(target = "revokedAt", source = "revokedAt")
    @Mapping(target = "lastEventId", source = "lastEventId")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ConsentView toDomain(ConsentProjectionJpaEntity entity);

    List<ConsentView> toDomainList(List<ConsentProjectionJpaEntity> entities);
}
