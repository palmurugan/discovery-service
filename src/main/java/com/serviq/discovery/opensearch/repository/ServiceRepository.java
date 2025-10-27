package com.serviq.discovery.opensearch.repository;

import com.serviq.discovery.opensearch.entity.ServiceDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ServiceRepository {

    private final OpenSearchClient openSearchClient;

    @Value("${opensearch.indexes.service-index}")
    private String serviceIndex;

    public String indexServiceDocument(ServiceDocument serviceDocument) throws IOException {
        log.debug("Indexing service document with serviceId: {}", serviceDocument.getServiceId());

        IndexRequest<ServiceDocument> request = IndexRequest.of(i -> i
                .index(serviceIndex)
                .id(serviceDocument.getServiceId())
                .document(serviceDocument)
                .refresh(Refresh.True));

        IndexResponse response = openSearchClient.index(request);

        log.info("Service indexed successfully with ID: {}, result: {}",
                response.id(), response.result());

        return response.id();
    }
}
