package com.serviq.discovery.service;

import com.serviq.discovery.dto.request.ServiceRequestDto;
import com.serviq.discovery.exception.ResourceNotFoundException;
import com.serviq.discovery.exception.ServiceIndexingException;
import com.serviq.discovery.mapper.ServiceMapper;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import com.serviq.discovery.opensearch.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.core.BulkResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

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

    /**
     * Get a service by ID from OpenSearch
     */
    public ServiceDocument getServiceById(String serviceId) {
        log.info("Fetching service with ID: {}", serviceId);
        try {
            return serviceRepository.getServiceById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Service not found with ID: " + serviceId
                    ));
        } catch (IOException e) {
            log.error("Failed to fetch service: {}", serviceId, e);
            throw new ServiceIndexingException(
                    "Failed to fetch service: " + serviceId, e
            );
        }
    }

    public List<ServiceDocument> searchServiceByKeyword(String searchKeyword) {
        log.info("Searching service with keyword: {}", searchKeyword);
        try {
            return serviceRepository.searchService(searchKeyword);
        } catch (IOException e) {
            log.error("Failed to search service by keyword", e);
            throw new ServiceIndexingException("Failed to search service by keyword", e);
        }
    }

    /**
     * Update an existing service in OpenSearch
     */
    public String updateService(String serviceId, ServiceRequestDto requestDto) {
        log.info("Updating service with ID: {}", serviceId);
        try {
            // Verify service exists
            ServiceDocument existingDocument = serviceRepository.getServiceById(serviceId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Service not found with ID: " + serviceId
                    ));

            // Update the document with new data
            serviceMapper.updateDocumentFromDto(requestDto, existingDocument);

            String documentId = serviceRepository.updateService(serviceId, existingDocument);

            log.info("Successfully updated service with ID: {}", documentId);
            return documentId;
        } catch (IOException e) {
            log.error("Failed to update service: {}", serviceId, e);
            throw new ServiceIndexingException(
                    "Failed to update service: " + serviceId, e
            );
        }
    }

    /**
     * Bulk index multiple services to OpenSearch
     */
    public BulkResponse bulkIndexServices(List<ServiceRequestDto> requestDtos) {
        log.info("Starting bulk indexing process for {} services", requestDtos.size());
        try {
            List<ServiceDocument> documents = requestDtos.stream()
                    .map(serviceMapper::toDocument)
                    .toList();

            BulkResponse response = serviceRepository.bulkIndexServices(documents);

            if (response.errors()) {
                log.warn("Bulk indexing completed with some errors");
            } else {
                log.info("Successfully bulk indexed {} services", requestDtos.size());
            }

            return response;
        } catch (IOException e) {
            log.error("Failed to bulk index services", e);
            throw new ServiceIndexingException("Failed to bulk index services", e);
        }
    }

    /**
     * Check if the index exists
     */
    public boolean checkIndexHealth() {
        try {
            return serviceRepository.indexExists();
        } catch (IOException e) {
            log.error("Failed to check index health", e);
            return false;
        }
    }

}
