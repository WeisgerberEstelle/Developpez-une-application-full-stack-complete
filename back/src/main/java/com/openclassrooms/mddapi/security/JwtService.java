package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT token management: generation, validation and subject (email) extraction.
 * Uses HMAC-SHA signing with a configured secret key.
 */
@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Generates a signed JWT token with the given email as subject.
     *
     * @param email the user's email used as token subject
     * @return a compact JWT string
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the email (subject) from a signed JWT token.
     *
     * @param token the JWT string
     * @return the email stored as subject
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates a JWT token by parsing and verifying its signature.
     *
     * @param token the JWT string to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Derives an HMAC-SHA key from the secret configured in application.properties. */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
