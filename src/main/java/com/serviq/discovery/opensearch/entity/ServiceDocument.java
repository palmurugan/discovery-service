package com.serviq.discovery.opensearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDocument {

    @JsonProperty("id")
    private String id;

    @JsonProperty("orgId")
    private String orgId;

    @JsonProperty("serviceId")
    private String serviceId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("categoryId")
    private String categoryId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("providerId")
    private String providerId;

    @JsonProperty("providerName")
    private String providerName;

    @JsonProperty("primaryLocation")
    private String primaryLocation;

    @JsonProperty("locations")
    private List<String> locations;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("createdAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonProperty("updatedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

}
