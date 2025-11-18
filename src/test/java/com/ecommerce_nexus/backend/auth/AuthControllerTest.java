package com.ecommerce_nexus.backend.auth;

import com.ecommerce_nexus.backend.auth.dto.AuthRequest;
import com.ecommerce_nexus.backend.auth.dto.AuthResponse;
import com.ecommerce_nexus.backend.auth.dto.RefreshTokenRequest;
import com.ecommerce_nexus.backend.auth.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AuthService authService;
    // Mock the JWT filter so it doesn't try to wire real security beans in slice test
    @MockBean com.ecommerce_nexus.backend.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void register_returnsTokens() throws Exception {
        Mockito.when(authService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthResponse("A1", "R1"));

        RegisterRequest req = new RegisterRequest();
        req.setEmail("john@example.com");
        req.setPassword("x");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken", is("A1")))
                .andExpect(jsonPath("$.refreshToken", is("R1")));
    }

    @Test
    void login_returnsTokens() throws Exception {
        Mockito.when(authService.login(any(AuthRequest.class)))
                .thenReturn(new AuthResponse("A2", "R2"));
        AuthRequest req = new AuthRequest();
        req.setEmail("a@b.com");
        req.setPassword("p");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("A2")))
                .andExpect(jsonPath("$.refreshToken", is("R2")));
    }

    @Test
    void refresh_returnsNewAccessToken() throws Exception {
        Mockito.when(authService.refresh("REF"))
                .thenReturn(new AuthResponse("NEW", "REF"));

        RefreshTokenRequest req = new RefreshTokenRequest();
        req.setRefreshToken("REF");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("NEW")))
                .andExpect(jsonPath("$.refreshToken", is("REF")));
    }
}
