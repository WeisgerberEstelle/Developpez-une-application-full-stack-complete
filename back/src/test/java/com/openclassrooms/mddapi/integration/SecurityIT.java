package com.openclassrooms.mddapi.integration;

import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("GET /api/posts/feed without token should return 401 or 403")
    void feedWithoutTokenShouldBeRejected() throws Exception {
        mockMvc.perform(get("/api/posts/feed"))
                .andExpect(status().is(anyOf(is(401), is(403))));
    }

    @Test
    @DisplayName("GET /api/posts/feed with valid token should return 200")
    void feedWithValidTokenShouldReturn200() throws Exception {
        String token = jwtService.generateToken("alice@example.com");

        mockMvc.perform(get("/api/posts/feed")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/posts/feed with invalid token should return 401 or 403")
    void feedWithInvalidTokenShouldBeRejected() throws Exception {
        mockMvc.perform(get("/api/posts/feed")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().is(anyOf(is(401), is(403))));
    }

    @Test
    @DisplayName("GET /api/user/me with valid token should return user data")
    void meWithValidTokenShouldReturnUser() throws Exception {
        String token = jwtService.generateToken("alice@example.com");

        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.subscriptions").isArray());
    }
}
