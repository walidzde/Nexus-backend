package com.ecommerce_nexus.backend.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrackOrderRequest {
    // Optional: track by numeric order ID
    private Long orderId;

    // Optional: track by public tracking code returned at checkout
    private String trackingCode;

    @NotBlank
    @Email
    private String email;
}
