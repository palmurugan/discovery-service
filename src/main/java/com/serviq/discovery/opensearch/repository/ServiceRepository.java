package com.serviq.discovery.opensearch.repository;

import com.serviq.discovery.opensearch.entity.ServiceDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.opensearch.core.bulk.IndexOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    public Optional<ServiceDocument> getServiceById(String serviceId) throws IOException {
        log.debug("Fetching service document with serviceId: {}", serviceId);

        GetRequest request = GetRequest.of(get -> get
                .index(serviceIndex)
                .id(serviceId)
        );

        GetResponse<ServiceDocument> response = openSearchClient.get(request, ServiceDocument.class);
        if (response.found()) {
            log.info("Service found with ID: {}", serviceId);
            return Optional.ofNullable(response.source());
        } else {
            log.info("Service not found with ID: {}", serviceId);
            return Optional.empty();
        }
    }

    public String updateService(String serviceId, ServiceDocument document) throws IOException {
        log.debug("Updating service document with serviceId: {}", serviceId);

        UpdateRequest<ServiceDocument, ServiceDocument> request = UpdateRequest.of(u -> u
                .index(serviceIndex)
                .id(serviceId)
                .doc(document)
                .refresh(org.opensearch.client.opensearch._types.Refresh.True)
        );

        UpdateResponse<ServiceDocument> response = openSearchClient.update(
                request,
                ServiceDocument.class
        );

        log.info("Service updated successfully with ID: {}, result: {}",
                response.id(), response.result());

        return response.id();
    }

    /**
     *
     * @param searchKeyword the keyword user used to search
     * @return List<ServiceDocument></ServiceDocument>
     * @throws IOException the exception
     */
    public List<ServiceDocument> searchService(String searchKeyword) throws IOException {
        log.debug("Searching service document with keyword: {}", searchKeyword);

        SearchRequest request = SearchRequest.of(s -> s
                .index(serviceIndex)
                .query(q -> q
                        .bool(b -> b
                                .should(sh -> sh
                                        .matchPhrasePrefix(m -> m
                                                .field("title")
                                                .query(searchKeyword)))
                                .minimumShouldMatch("1"))
                )
        );
        SearchResponse<ServiceDocument> response = openSearchClient.search(request, ServiceDocument.class);

        // Convert hits to list of ServiceDocument
        return response.hits().hits().stream()
                .map(hit -> {
                    ServiceDocument doc = hit.source();
                    if (doc != null) {
                        doc.setId(hit.id());
                    }
                    return doc;
                })
                .toList();
    }

    /**
     * Bulk index multiple service documents
     */
    public BulkResponse bulkIndexServices(List<ServiceDocument> documents) throws IOException {
        log.debug("Bulk indexing {} service documents", documents.size());

        List<BulkOperation> bulkOperations = documents.stream()
                .map(doc -> BulkOperation.of(b -> b
                        .index(IndexOperation.of(i -> i
                                .index(serviceIndex)
                                .id(doc.getServiceId())
                                .document(doc)
                        ))
                )).toList();

        BulkRequest request = BulkRequest.of(r -> r
                .index(serviceIndex)
                .operations(bulkOperations)
                .refresh(org.opensearch.client.opensearch._types.Refresh.True)
        );

        BulkResponse response = openSearchClient.bulk(request);

        if (response.errors()) {
            log.error("Bulk indexing completed with errors");
            response.items().forEach(item -> {
                if (item.error() != null) {
                    log.error("Error indexing document {}: {}",
                            item.id(), item.error().reason());
                }
            });
        } else {
            log.info("Bulk indexing completed successfully for {} documents",
                    documents.size());
        }

        return response;
    }

    /**
     * Check if index exists
     */
    public boolean indexExists() throws IOException {
        return openSearchClient.indices().exists(e -> e.index(serviceIndex)).value();
    }
}
