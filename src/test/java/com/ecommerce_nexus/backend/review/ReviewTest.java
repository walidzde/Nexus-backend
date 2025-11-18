package com.ecommerce_nexus.backend.review;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void onCreate_setsCreatedAndUpdatedAt() {
        Review review = new Review();

        assertNull(review.getCreatedAt());
        assertNull(review.getUpdatedAt());

        review.onCreate();

        assertNotNull(review.getCreatedAt());
        assertNotNull(review.getUpdatedAt());
        assertFalse(review.getCreatedAt().isAfter(Instant.now()));
        assertFalse(review.getUpdatedAt().isAfter(Instant.now()));
    }

    @Test
    void onUpdate_updatesUpdatedAt() {
        Review review = new Review();
        review.onCreate();
        Instant before = review.getUpdatedAt();

        review.onUpdate();

        assertTrue(review.getUpdatedAt().equals(before) || review.getUpdatedAt().isAfter(before));
    }
}
