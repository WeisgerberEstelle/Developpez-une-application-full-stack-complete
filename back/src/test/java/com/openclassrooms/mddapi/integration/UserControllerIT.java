package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    private String bobToken;

    @BeforeEach
    void setUp() {
        bobToken = jwtService.generateToken("bob@example.com");
    }

    @Test
    @DisplayName("GET /api/user/me should return user with subscriptions")
    void meShouldReturnUserWithSubscriptions() throws Exception {
        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", "Bearer " + bobToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.subscriptions").isArray())
                .andExpect(jsonPath("$.subscriptions.length()").value(greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("PUT /api/user/me with partial update (username only) should update only that field")
    void updateUsernameOnlyShouldWork() throws Exception {
        String newUsername = "bob-updated-" + System.currentTimeMillis();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername(newUsername);

        mockMvc.perform(put("/api/user/me")
                        .header("Authorization", "Bearer " + bobToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(newUsername))
                .andExpect(jsonPath("$.email").value("bob@example.com")); // email unchanged

        // restore original username
        UpdateUserRequest restore = new UpdateUserRequest();
        restore.setUsername("bob");
        mockMvc.perform(put("/api/user/me")
                .header("Authorization", "Bearer " + bobToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restore)));
    }

    @Test
    @DisplayName("PUT /api/user/me with email already used by another user should return 409")
    void updateWithDuplicateEmailShouldReturn409() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("alice@example.com"); // alice's email

        mockMvc.perform(put("/api/user/me")
                        .header("Authorization", "Bearer " + bobToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already in use"));
    }

    @Test
    @DisplayName("PUT /api/user/me with username already taken should return 409")
    void updateWithDuplicateUsernameShouldReturn409() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("alice"); // alice's username

        mockMvc.perform(put("/api/user/me")
                        .header("Authorization", "Bearer " + bobToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username already in use"));
    }
}
