package com.openclassrooms.mddapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerIT {

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
    @DisplayName("GET /api/posts/{postId}/comments should return comments for the post")
    void getByPostIdShouldReturnComments() throws Exception {
        mockMvc.perform(get("/api/posts/1/comments")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /api/posts/{postId}/comments should create a comment")
    void createShouldReturnNewComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Integration test comment");

        mockMvc.perform(post("/api/posts/1/comments")
                        .header("Authorization", "Bearer " + aliceToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Integration test comment"))
                .andExpect(jsonPath("$.authorUsername").value("alice"));
    }

    @Test
    @DisplayName("POST /api/posts/{postId}/comments with blank content should return 400")
    void createWithBlankContentShouldReturn400() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("");

        mockMvc.perform(post("/api/posts/1/comments")
                        .header("Authorization", "Bearer " + aliceToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
