package com.ecommerce_nexus.backend.auth;

import com.ecommerce_nexus.backend.auth.dto.AuthRequest;
import com.ecommerce_nexus.backend.auth.dto.AuthResponse;
import com.ecommerce_nexus.backend.auth.dto.RegisterRequest;
import com.ecommerce_nexus.backend.security.JwtService;
import com.ecommerce_nexus.backend.user.Role;
import com.ecommerce_nexus.backend.user.User;
import com.ecommerce_nexus.backend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthService authService;

    @BeforeEach
    void setup() {
        // no-op
    }

    @Test
    void register_createsUser_andReturnsTokens() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("john@example.com");
        req.setPassword("plain");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPhone("123");
        req.setAddress("Somewhere");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain")).thenReturn("ENC");
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token");

        AuthResponse resp = authService.register(req);

        assertEquals("access-token", resp.getAccessToken());
        assertEquals("refresh-token", resp.getRefreshToken());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("john@example.com", saved.getEmail());
        assertEquals("ENC", saved.getPassword());
        assertEquals(Role.USER, saved.getRole());
        assertNotNull(saved.getProfile());
        assertEquals("John", saved.getProfile().getFirstName());
        assertSame(saved, saved.getProfile().getUser());
    }

    @Test
    void register_throwsWhenEmailExists() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("taken@example.com");
        req.setPassword("x");
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsTokensOnSuccess() {
        AuthRequest req = new AuthRequest();
        req.setEmail("a@b.com");
        req.setPassword("pwd");

        User user = User.builder().email("a@b.com").password("enc").build();
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtService.generateAccessToken(user)).thenReturn("A");
        when(jwtService.generateRefreshToken(user)).thenReturn("R");

        AuthResponse resp = authService.login(req);
        assertEquals("A", resp.getAccessToken());
        assertEquals("R", resp.getRefreshToken());
    }

    @Test
    void refresh_returnsNewAccessToken_whenValid() {
        String refresh = "refresh-123";
        User user = User.builder().email("x@y.com").password("p").build();
        when(jwtService.extractUsername(refresh)).thenReturn("x@y.com");
        when(userRepository.findByEmail("x@y.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshToken(refresh)).thenReturn(true);
        when(jwtService.isTokenValid(refresh, user)).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn("new-access");

        AuthResponse resp = authService.refresh(refresh);
        assertEquals("new-access", resp.getAccessToken());
        assertEquals(refresh, resp.getRefreshToken());
    }

    @Test
    void refresh_throwsOnInvalidRefreshToken() {
        String refresh = "bad";
        User user = User.builder().email("u@e.com").build();
        when(jwtService.extractUsername(refresh)).thenReturn("u@e.com");
        when(userRepository.findByEmail("u@e.com")).thenReturn(Optional.of(user));
        when(jwtService.isRefreshToken(refresh)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.refresh(refresh));
    }
}
