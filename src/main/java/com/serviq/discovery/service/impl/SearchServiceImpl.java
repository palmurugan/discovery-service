package com.serviq.discovery.service.impl;

import com.serviq.discovery.dto.request.SearchRequest;
import com.serviq.discovery.exception.BusinessException;
import com.serviq.discovery.mapper.ServiceMapper;
import com.serviq.discovery.opensearch.entity.ServiceDocument;
import com.serviq.discovery.opensearch.repository.ServiceRepository;
import com.serviq.discovery.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public List<ServiceDocument> searchServiceByKeyword(SearchRequest request) {
        log.info("Searching service with keyword: {}", request.getKeyword());
        try {
            return serviceRepository.searchService(request.getKeyword());
        } catch (IOException e) {
            log.error("Failed to search service by keyword", e);
            throw new BusinessException("Failed to search service by keyword", "SEARCH_FAILED");
        }
    }
}
