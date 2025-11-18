package com.ecommerce_nexus.backend.category;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void onCreate_setsCreatedAndUpdatedAt() {
        Category category = new Category();
        assertNull(category.getCreatedAt());
        assertNull(category.getUpdatedAt());

        category.onCreate();

        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertFalse(category.getCreatedAt().isAfter(Instant.now()));
    }

    @Test
    void onUpdate_updatesUpdatedAt_only() {
        Category category = new Category();
        category.onCreate();
        Instant createdBefore = category.getCreatedAt();
        Instant updatedBefore = category.getUpdatedAt();

        category.onUpdate();

        assertEquals(createdBefore, category.getCreatedAt());
        assertTrue(category.getUpdatedAt().equals(updatedBefore) || category.getUpdatedAt().isAfter(updatedBefore));
    }
}
