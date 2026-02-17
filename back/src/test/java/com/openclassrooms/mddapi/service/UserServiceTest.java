package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User buildUser() {
        return User.builder().id(1L).email("alice@example.com").username("alice").password("encoded").build();
    }

    @Test
    void getCurrentUser_shouldReturnMappedResponse() {
        User user = buildUser();
        UserResponse expected = new UserResponse();
        expected.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expected);

        UserResponse result = userService.getCurrentUser(user);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updateUser_shouldUpdateOnlyProvidedFields() {
        User user = buildUser();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("alice-new");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("alice-new")).thenReturn(false);
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        userService.updateUser(user, request);

        assertThat(user.getUsername()).isEqualTo("alice-new");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void updateUser_shouldEncodePasswordWhenProvided() {
        User user = buildUser();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPassword("NewPassword1!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPassword1!")).thenReturn("new-encoded");
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        userService.updateUser(user, request);

        assertThat(user.getPassword()).isEqualTo("new-encoded");
    }

    @Test
    void updateUser_shouldThrowWhenEmailAlreadyTaken() {
        User user = buildUser();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("taken@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(user, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already in use");
    }

    @Test
    void updateUser_shouldThrowWhenUsernameAlreadyTaken() {
        User user = buildUser();
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("taken");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(user, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already in use");
    }
}
