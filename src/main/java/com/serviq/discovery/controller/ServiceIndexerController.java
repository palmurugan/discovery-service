package com.serviq.discovery.controller;

import com.serviq.discovery.dto.request.ServiceRequestDto;
import com.serviq.discovery.service.ServiceIndexerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
