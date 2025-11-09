package com.serviq.discovery.service;

import com.serviq.discovery.dto.request.SearchRequest;
import com.serviq.discovery.opensearch.entity.ServiceDocument;

import java.util.List;

public interface SearchService {

    List<ServiceDocument> searchServiceByKeyword(SearchRequest request);
}
