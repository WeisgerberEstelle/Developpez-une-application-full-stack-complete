package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor 
public class UserService {

    private final UserMapper userMapper;

    public UserResponse getCurrentUser(User user) {
        UserResponse response = userMapper.toResponse(user);
        response.setSubscriptions(Collections.emptyList());
        return response;
    }
}
