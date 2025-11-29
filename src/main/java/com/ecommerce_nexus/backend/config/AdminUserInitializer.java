package com.ecommerce_nexus.backend.config;

import com.ecommerce_nexus.backend.user.Role;
import com.ecommerce_nexus.backend.user.User;
import com.ecommerce_nexus.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class AdminUserInitializer {

    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:#{null}}")
    private String configuredAdminUsername;

    @Value("${admin.password:#{null}}")
    private String configuredAdminPassword;

    @Bean
    public CommandLineRunner ensureAdminUser() {
        return args -> {
            // Defaults if not provided via properties/env
            String adminUsername = (configuredAdminUsername == null || configuredAdminUsername.isBlank())
                    ? "admin" : configuredAdminUsername.trim();
            String adminPassword = (configuredAdminPassword == null || configuredAdminPassword.isBlank())
                    ? "admin123" : configuredAdminPassword;

            // The application uses email as username field, so we store "admin" in the email column.
            boolean exists = userRepository.existsByEmail(adminUsername);
            if (exists) {
                log.info("Admin user '{}' already exists — skipping initialization.", adminUsername);
                return;
            }

            User admin = User.builder()
                    .email(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .createdAt(Instant.now())
                    .build();

            userRepository.save(admin);
            log.info("Admin user '{}' has been created.", adminUsername);
        };
    }
}
