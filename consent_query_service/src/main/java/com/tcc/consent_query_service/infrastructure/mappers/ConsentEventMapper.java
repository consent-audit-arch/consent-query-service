package com.tcc.consent_query_service.infrastructure.mappers;

import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentEventJpaEntity;
import com.tcc.consent_query_service.model.consent.valueObjects.ConsentHistoryEntry;
import com.tcc.consent_query_service.model.consent.valueObjects.IssuedBy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsentEventMapper {

    @Mapping(target = "issuedBy", expression = "java(IssuedBy.fromJson(source.getIssuedBy()))")
    ConsentHistoryEntry toDomain(ConsentEventJpaEntity source);

    List<ConsentHistoryEntry> toDomainList(List<ConsentEventJpaEntity> sources);
}