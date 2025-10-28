package com.serviq.discovery.controller;

import com.serviq.discovery.dto.request.SearchRequest;
import com.serviq.discovery.dto.request.ServiceRequestDto;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import com.serviq.discovery.service.ServiceIndexerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/service-indexer")
@RequiredArgsConstructor
public class ServiceIndexerController {

    private final ServiceIndexerService serviceIndexerService;

    @PostMapping
    public ResponseEntity<String> indexServiceDocument(@Valid @RequestBody ServiceRequestDto requestDto) {
        log.info("Received request to index service: {}", requestDto.getServiceId());

        String documentId = serviceIndexerService.indexServiceDocument(requestDto);
        return ResponseEntity.ok(documentId);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDocument> getService(
            @PathVariable String serviceId) {
        log.info("Received request to get service: {}", serviceId);

        ServiceDocument document = serviceIndexerService.getServiceById(serviceId);
        return ResponseEntity.ok(document);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ServiceDocument>> searchService(@Valid @RequestBody SearchRequest searchRequest) {
        log.info("Received request to search service: {}", searchRequest.getKeyword());

        List<ServiceDocument> documents = serviceIndexerService.searchServiceByKeyword(searchRequest.getKeyword());
        return ResponseEntity.ok(documents);
    }

    /**
     * Update an existing service
     * PUT /api/v1/services/{serviceId}
     */
    @PutMapping("/{serviceId}")
    public ResponseEntity<String> updateService(
            @PathVariable String serviceId,
            @Valid @RequestBody ServiceRequestDto requestDto) {
        log.info("Received request to update service: {}", serviceId);

        String documentId = serviceIndexerService.updateService(serviceId, requestDto);
        return ResponseEntity.ok(documentId);
    }
}
