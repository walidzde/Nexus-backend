package com.ecommerce_nexus.backend.product.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class ProductResponse {
    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer stock;
    String imageUrl;
    Long categoryId;
    String categoryName;
    Instant createdAt;
    Instant updatedAt;
}
