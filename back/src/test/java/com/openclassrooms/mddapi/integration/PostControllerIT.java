package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.CreatePostRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    private String aliceToken;

    @BeforeEach
    void setUp() {
        aliceToken = jwtService.generateToken("alice@example.com");
    }

    @Test
    @DisplayName("GET /api/posts/feed should return only posts from subscribed topics")
    void feedShouldReturnSubscribedPosts() throws Exception {
        // alice is subscribed to Java (1), Angular (4), Spring Boot (5)
        mockMvc.perform(get("/api/posts/feed")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].topicName",
                        everyItem(is(oneOf("Java", "Angular", "Spring Boot")))));
    }

    @Test
    @DisplayName("GET /api/posts/{id} should return the post with mapped fields")
    void getByIdShouldReturnPost() throws Exception {
        mockMvc.perform(get("/api/posts/1")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Bien debuter avec Spring Boot 3"))
                .andExpect(jsonPath("$.authorUsername").value("alice"))
                .andExpect(jsonPath("$.topicName").value("Spring Boot"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/posts should create a post and return it with mapped fields")
    void createShouldReturnNewPost() throws Exception {
        CreatePostRequest request = new CreatePostRequest();
        request.setTopicId(1L); // Java
        request.setTitle("Integration test post");
        request.setContent("Content from integration test");

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + aliceToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration test post"))
                .andExpect(jsonPath("$.authorUsername").value("alice"))
                .andExpect(jsonPath("$.topicName").value("Java"));
    }

    @Test
    @DisplayName("POST /api/posts with missing title should return 400")
    void createWithMissingTitleShouldReturn400() throws Exception {
        CreatePostRequest request = new CreatePostRequest();
        request.setTopicId(1L);
        request.setTitle("");
        request.setContent("Some content");

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + aliceToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
