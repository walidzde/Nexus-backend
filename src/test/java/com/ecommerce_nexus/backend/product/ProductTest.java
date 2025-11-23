package com.ecommerce_nexus.backend.product;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void onCreate_setsCreatedAtAndUpdatedAt() {
        Product product = new Product();
        assertNull(product.getCreatedAt());
        assertNull(product.getUpdatedAt());

        product.onCreate();

        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
        assertFalse(product.getCreatedAt().isAfter(Instant.now()));
        assertFalse(product.getUpdatedAt().isAfter(Instant.now()));
    }

    @Test
    void onUpdate_updatesUpdatedAtOnly() throws InterruptedException {
        Product product = new Product();
        product.onCreate();
        Instant createdAtBefore = product.getCreatedAt();
        Instant updatedAtBefore = product.getUpdatedAt();

        product.onUpdate();

        assertEquals(createdAtBefore, product.getCreatedAt());
        assertTrue(product.getUpdatedAt().equals(updatedAtBefore) || product.getUpdatedAt().isAfter(updatedAtBefore));
    }
}
