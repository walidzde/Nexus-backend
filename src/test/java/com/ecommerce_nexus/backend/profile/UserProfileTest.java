package com.ecommerce_nexus.backend.profile;

import com.ecommerce_nexus.backend.user.User;
import com.ecommerce_nexus.backend.user.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void builder_setsFields_andAssociationWithUser() {
        User user = User.builder().email("a@b.com").password("x").build();

        UserProfile profile = UserProfile.builder()
                .firstName("Jane")
                .lastName("Doe")
                .phone("123")
                .address("Somewhere")
                .user(user)
                .build();

        assertEquals("Jane", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("123", profile.getPhone());
        assertEquals("Somewhere", profile.getAddress());
        assertSame(user, profile.getUser());
    }
}
