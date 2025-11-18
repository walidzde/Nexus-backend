package com.ecommerce_nexus.backend.profile;

import com.ecommerce_nexus.backend.profile.dto.ProfileResponse;
import com.ecommerce_nexus.backend.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ProfileResponse.fromUser(user));
    }
}
