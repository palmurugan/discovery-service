package com.serviq.discovery.controller;

import com.serviq.discovery.dto.request.SearchRequest;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import com.serviq.discovery.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<List<ServiceDocument>> searchService(@Valid @RequestBody SearchRequest searchRequest) {
        log.info("Received request to search service: {}", searchRequest.getKeyword());

        List<ServiceDocument> documents = searchService.searchServiceByKeyword(searchRequest);
        return ResponseEntity.ok(documents);
    }
}
