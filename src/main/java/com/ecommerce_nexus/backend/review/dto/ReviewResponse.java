package com.ecommerce_nexus.backend.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long productId;
    private Long userId;
    private String userEmail;
    private int rating;
    private String comment;
    private Instant createdAt;
    private Instant updatedAt;
}
