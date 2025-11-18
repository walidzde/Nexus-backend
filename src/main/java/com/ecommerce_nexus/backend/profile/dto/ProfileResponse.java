package com.ecommerce_nexus.backend.profile.dto;

import com.ecommerce_nexus.backend.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public static ProfileResponse fromUser(User user) {
        var p = user.getProfile();
        return new ProfileResponse(
                user.getId(),
                user.getEmail(),
                p != null ? p.getFirstName() : null,
                p != null ? p.getLastName() : null,
                p != null ? p.getPhone() : null,
                p != null ? p.getAddress() : null
        );
    }
}
