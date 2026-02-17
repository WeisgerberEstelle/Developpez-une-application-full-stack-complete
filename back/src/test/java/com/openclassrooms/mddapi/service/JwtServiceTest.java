package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "test-secret-key-that-is-long-enough-for-hs256-algorithm");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 86400000L);
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtService.generateToken("alice@example.com");
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void getEmailFromToken_shouldExtractEmail() {
        String token = jwtService.generateToken("alice@example.com");
        String email = jwtService.getEmailFromToken(token);
        assertThat(email).isEqualTo("alice@example.com");
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken("alice@example.com");
        assertThat(jwtService.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertThat(jwtService.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", -1000L);
        String token = jwtService.generateToken("alice@example.com");
        assertThat(jwtService.validateToken(token)).isFalse();
    }
}
