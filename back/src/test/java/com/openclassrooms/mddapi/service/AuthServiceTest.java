package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AuthResponse;
import com.openclassrooms.mddapi.dto.LoginRequest;
import com.openclassrooms.mddapi.dto.RegisterRequest;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.DuplicateResourceException;
import com.openclassrooms.mddapi.exception.UnauthorizedException;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldEncodePasswordAndReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setUsername("newuser");
        request.setPassword("Password1!");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
        when(jwtService.generateToken("new@example.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(passwordEncoder).encode("Password1!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setUsername("newuser");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already in use");
    }

    @Test
    void register_shouldThrowWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setUsername("taken");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Username already taken");
    }

    @Test
    void login_shouldReturnTokenWithValidEmail() {
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("alice@example.com");
        request.setPassword("Password1!");

        User user = User.builder().email("alice@example.com").password("encoded").build();
        when(userRepository.findByEmailOrUsername("alice@example.com", "alice@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);
        when(jwtService.generateToken("alice@example.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void login_shouldReturnTokenWithValidUsername() {
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("alice");
        request.setPassword("Password1!");

        User user = User.builder().email("alice@example.com").password("encoded").build();
        when(userRepository.findByEmailOrUsername("alice", "alice"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);
        when(jwtService.generateToken("alice@example.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void login_shouldThrowWithInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmailOrUsername("alice@example.com");
        request.setPassword("wrong");

        User user = User.builder().email("alice@example.com").password("encoded").build();
        when(userRepository.findByEmailOrUsername("alice@example.com", "alice@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Invalid credentials");
    }
}
