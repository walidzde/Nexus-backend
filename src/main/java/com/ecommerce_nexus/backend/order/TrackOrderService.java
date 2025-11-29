package com.ecommerce_nexus.backend.order;

import com.ecommerce_nexus.backend.order.dto.TrackOrderRequest;
import com.ecommerce_nexus.backend.order.dto.TrackOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TrackOrderService {

    private final OrderRepository orderRepository;

    // Hardcoded list of statuses to simulate movement
    private static final List<String> STATUS_FLOW = List.of(
            "Order received",
            "Payment confirmed",
            "Items being prepared",
            "Packed at warehouse",
            "Shipped",
            "Out for delivery",
            "Delivered"
    );

    // Track progress per (ref,email) where ref is either id:<id> or code:<trackingCode>
    private final Map<Key, Integer> progress = new ConcurrentHashMap<>();

    public TrackOrderResponse track(TrackOrderRequest req) {
        Long orderId = req.getOrderId();
        String trackingCode = req.getTrackingCode();
        String email = req.getEmail();

        String ref;
        if (trackingCode != null && !trackingCode.isBlank()) {
            orderRepository.findByTrackingCode(trackingCode)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found for tracking code: " + trackingCode));
            ref = "code:" + trackingCode;
        } else if (orderId != null) {
            orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
            ref = "id:" + orderId;
        } else {
            throw new IllegalArgumentException("Either trackingCode or orderId must be provided");
        }

        Key key = new Key(ref, email.toLowerCase());
        int current = progress.getOrDefault(key, -1);
        if (current < STATUS_FLOW.size() - 1) {
            current++;
            progress.put(key, current);
        }

        List<String> steps = new ArrayList<>(STATUS_FLOW.subList(0, Math.max(0, current + 1)));
        return TrackOrderResponse.builder()
                .orderId(orderId)
                .email(email)
                .steps(steps)
                .build();
    }

    private record Key(String ref, String email) {
        public Key {
            Objects.requireNonNull(ref);
            Objects.requireNonNull(email);
        }
    }
}
