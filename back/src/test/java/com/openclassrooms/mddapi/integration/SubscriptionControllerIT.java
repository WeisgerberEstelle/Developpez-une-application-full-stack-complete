package com.openclassrooms.mddapi.integration;

import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String aliceToken;

    @BeforeEach
    void setUp() {
        aliceToken = jwtService.generateToken("alice@example.com");
    }

    @Test
    @DisplayName("POST /api/topics/{id}/subscribe should subscribe user to a new topic")
    void subscribeShouldSucceed() throws Exception {
        // alice is NOT subscribed to topic 2 (JavaScript) in seed data
        mockMvc.perform(post("/api/topics/2/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk());

        // cleanup: unsubscribe after test
        mockMvc.perform(delete("/api/topics/2/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/topics/{id}/subscribe twice should return 409")
    void subscribeToAlreadySubscribedTopicShouldReturn409() throws Exception {
        // alice is already subscribed to topic 1 (Java)
        mockMvc.perform(post("/api/topics/1/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Already subscribed to this topic"));
    }

    @Test
    @DisplayName("DELETE /api/topics/{id}/subscribe should unsubscribe user")
    void unsubscribeShouldSucceed() throws Exception {
        // subscribe first to topic 3 (Python), then unsubscribe
        mockMvc.perform(post("/api/topics/3/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/topics/3/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/topics/{id}/subscribe when not subscribed should return 404")
    void unsubscribeWhenNotSubscribedShouldReturn404() throws Exception {
        // alice is NOT subscribed to topic 7 (Databases)
        mockMvc.perform(delete("/api/topics/7/subscribe")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not subscribed to this topic"));
    }
}
