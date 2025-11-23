package com.ecommerce_nexus.backend.order;

import com.ecommerce_nexus.backend.order.dto.CheckoutRequest;
import com.ecommerce_nexus.backend.order.dto.OrderItemResponse;
import com.ecommerce_nexus.backend.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        Order order = orderService.placeOrderFromCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> myOrders() {
        List<OrderResponse> list = orderService.getMyOrders().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(orderService.getMyOrder(id)));
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream().map(oi ->
                OrderItemResponse.builder()
                        .productId(oi.getProduct().getId())
                        .productName(oi.getProduct().getName())
                        .quantity(oi.getQuantity())
                        .unitPrice(oi.getUnitPrice())
                        .lineTotal(oi.getUnitPrice().multiply(java.math.BigDecimal.valueOf(oi.getQuantity())))
                        .build()
        ).toList();
        return OrderResponse.builder()
                .id(o.getId())
                .createdAt(o.getCreatedAt())
                .total(o.getTotal())
                .status(o.getStatus())
                .paymentStatus(o.getPaymentStatus())
                .shippingAddress(o.getShippingAddress())
                .items(items)
                .build();
    }
}
