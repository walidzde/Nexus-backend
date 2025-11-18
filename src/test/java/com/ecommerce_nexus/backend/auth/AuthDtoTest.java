package com.ecommerce_nexus.backend.auth;

import com.ecommerce_nexus.backend.auth.dto.AuthRequest;
import com.ecommerce_nexus.backend.auth.dto.AuthResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoTest {

    @Test
    void authResponse_holdsTokens() {
        AuthResponse resp = new AuthResponse("access-123", "refresh-456");
        assertEquals("access-123", resp.getAccessToken());
        assertEquals("refresh-456", resp.getRefreshToken());
    }

    @Test
    void authRequest_settersAndGettersWork() {
        AuthRequest req = new AuthRequest();
        req.setEmail("john@example.com");
        req.setPassword("secret");

        assertEquals("john@example.com", req.getEmail());
        assertEquals("secret", req.getPassword());
    }
}
