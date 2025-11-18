package com.ecommerce_nexus.backend.cart;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void onCreate_setsUpdatedAt() {
        Cart cart = new Cart();
        assertNull(cart.getUpdatedAt());

        cart.onCreate();

        assertNotNull(cart.getUpdatedAt());
        assertFalse(cart.getUpdatedAt().isAfter(Instant.now()));
    }

    @Test
    void onUpdate_updatesUpdatedAt() {
        Cart cart = new Cart();
        cart.onCreate();
        Instant before = cart.getUpdatedAt();

        cart.onUpdate();

        assertTrue(cart.getUpdatedAt().equals(before) || cart.getUpdatedAt().isAfter(before));
    }
}
