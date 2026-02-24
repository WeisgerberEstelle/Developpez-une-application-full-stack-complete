package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UpdateUserRequest;
import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.DuplicateResourceException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for retrieving and updating user profiles.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(managedUser);
    }

    @Transactional
    /**
     * Partially updates the user's profile.
     * Only checks for duplicates if the value actually changed
     * (avoids false positive when user re-submits the same email/username).
     *
     * @param user    the authenticated user
     * @param request the fields to update (nullable fields are skipped)
     * @return the updated user profile
     * @throws DuplicateResourceException if new email or username is already taken
     */
    public UserResponse updateUser(User user, UpdateUserRequest request) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail()) &&
                    !managedUser.getEmail().equals(request.getEmail())) {
                throw new DuplicateResourceException("Email already in use");
            }
            managedUser.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            if (userRepository.existsByUsername(request.getUsername()) &&
                    !managedUser.getUsername().equals(request.getUsername())) {
                throw new DuplicateResourceException("Username already in use");
            }
            managedUser.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            managedUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(managedUser);
        return userMapper.toResponse(managedUser);
    }
}
