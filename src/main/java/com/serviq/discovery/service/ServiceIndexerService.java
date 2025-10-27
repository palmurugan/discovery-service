package com.serviq.discovery.service;

import com.serviq.discovery.dto.request.ServiceRequestDto;
import com.serviq.discovery.mapper.ServiceMapper;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import com.serviq.discovery.opensearch.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceIndexerService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    public String indexServiceDocument(ServiceRequestDto requestDto) {
        log.info("Starting indexing process for service: {}", requestDto.getServiceId());

        try {
            ServiceDocument serviceDocument = serviceMapper.toDocument(requestDto);
            serviceDocument.setCreatedAt(Instant.now());
            serviceDocument.setUpdatedAt(Instant.now());
            return serviceRepository.indexServiceDocument(serviceDocument);
        } catch (IOException e) {
            log.error("Failed to index service: {}", requestDto.getServiceId(), e);
            throw new RuntimeException(
                    "Failed to index service: " + requestDto.getServiceId(), e
            );
        }
    }

}
