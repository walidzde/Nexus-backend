package com.ecommerce_nexus.backend.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackOrderResponse {
    private Long orderId;
    private String email;
    private List<String> steps;
}
