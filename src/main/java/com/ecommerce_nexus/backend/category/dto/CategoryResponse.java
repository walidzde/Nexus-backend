package com.ecommerce_nexus.backend.category.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class CategoryResponse {
    Long id;
    String name;
    String coverImage;
    Instant createdAt;
    Instant updatedAt;
}
