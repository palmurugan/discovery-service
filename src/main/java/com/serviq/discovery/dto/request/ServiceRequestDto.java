package com.serviq.discovery.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {

    @NotBlank(message = "Organization ID is required")

    private String orgId;

    @NotBlank(message = "Service ID is required")
    private String serviceId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 100, message = "Category must be between 2 and 100 characters")
    private String category;

    @NotBlank(message = "Provider ID is required")
    private String providerId;

    @NotBlank(message = "Provider name is required")
    @Size(min = 2, max = 200, message = "Provider name must be between 2 and 200 characters")
    private String providerName;

    @NotBlank(message = "Primary location is required")
    @Size(min = 5, max = 300, message = "Primary location must be between 5 and 300 characters")
    private String primaryLocation;

    @NotEmpty(message = "At least one location is required")
    @Size(min = 1, max = 10, message = "Number of locations must be between 1 and 10")
    private List<@NotBlank(message = "Location cannot be blank") String> locations;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1")
    @Max(value = 1440, message = "Duration cannot exceed 1440 minutes (24 hours)")
    private Integer duration;

    @NotBlank(message = "Unit is required")
    @Pattern(regexp = "^(minutes|hours|days)$", message = "Unit must be one of: minutes, hours, days")
    private String unit;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have maximum 10 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter ISO code")
    private String currency;
}
