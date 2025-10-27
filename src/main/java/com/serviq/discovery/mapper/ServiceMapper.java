package com.serviq.discovery.mapper;

import com.serviq.discovery.dto.request.ServiceRequestDto;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    ServiceDocument toDocument(ServiceRequestDto dto);

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDocumentFromDto(ServiceRequestDto dto, @MappingTarget ServiceDocument document);
}
