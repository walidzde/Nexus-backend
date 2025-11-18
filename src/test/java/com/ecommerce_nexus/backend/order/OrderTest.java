package com.ecommerce_nexus.backend.order;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void onCreate_setsCreatedAt() {
        Order order = new Order();
        assertNull(order.getCreatedAt());

        order.onCreate();

        assertNotNull(order.getCreatedAt());
        assertFalse(order.getCreatedAt().isAfter(Instant.now()));
    }
}
