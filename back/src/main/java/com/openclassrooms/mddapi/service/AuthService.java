package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles user registration and login.
 * Returns a JWT token on successful authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registers a new user after checking for email/username uniqueness.
     *
     * @param request the registration data
     * @return an {@link AuthResponse} containing the JWT token
     * @throws IllegalArgumentException if email or username already exists
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    /**
     * Authenticates a user by email or username and password.
     *
     * @param request the login credentials
     * @return an {@link AuthResponse} containing the JWT token
     * @throws IllegalArgumentException if credentials are invalid
     */
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getEmailOrUsername();

        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
