package com.ecommerce_nexus.backend.order.dto;

import com.ecommerce_nexus.backend.order.Address;
import com.ecommerce_nexus.backend.order.OrderStatus;
import com.ecommerce_nexus.backend.order.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Instant createdAt;
    private BigDecimal total;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Address shippingAddress;
    private String email;
    private String trackingCode;
    private List<OrderItemResponse> items;
}
