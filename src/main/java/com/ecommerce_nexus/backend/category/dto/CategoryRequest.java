package com.ecommerce_nexus.backend.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank
    private String name;
    // Optional cover image URL
    private String coverImage;
}
