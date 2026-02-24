package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /api/auth/register")
    class Register {

        @Test
        @DisplayName("should register a new user and return a JWT token")
        void registerWithValidData() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("newuser-" + System.currentTimeMillis() + "@test.com");
            request.setUsername("newuser" + System.currentTimeMillis());
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("should return 400 when email is invalid")
        void registerWithInvalidEmail() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("not-an-email");
            request.setUsername("validuser");
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.email").exists());
        }

        @Test
        @DisplayName("should return 400 when password is too weak")
        void registerWithWeakPassword() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("weak@test.com");
            request.setUsername("weakuser");
            request.setPassword("password");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.password").exists());
        }

        @Test
        @DisplayName("should return 409 when email is already in use")
        void registerWithDuplicateEmail() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("alice@example.com"); // seed data
            request.setUsername("uniqueuser");
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error").value("Email already in use"));
        }

        @Test
        @DisplayName("should return 400 when required fields are blank")
        void registerWithBlankFields() throws Exception {
            RegisterRequest request = new RegisterRequest();
            request.setEmail("");
            request.setUsername("");
            request.setPassword("");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("should login with valid email and password")
        void loginWithEmail() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setEmailOrUsername("alice@example.com");
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("should login with valid username and password")
        void loginWithUsername() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setEmailOrUsername("alice");
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("should return 401 with wrong password")
        void loginWithWrongPassword() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setEmailOrUsername("alice@example.com");
            request.setPassword("WrongPass1!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("Invalid credentials"));
        }

        @Test
        @DisplayName("should return 401 with unknown identifier")
        void loginWithUnknownUser() throws Exception {
            LoginRequest request = new LoginRequest();
            request.setEmailOrUsername("nobody@test.com");
            request.setPassword("Password1!");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("Invalid credentials"));
        }
    }
}
