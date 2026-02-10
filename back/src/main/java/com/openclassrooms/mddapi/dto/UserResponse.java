package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private List<TopicResponse> subscriptions;
}
