package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorUsername;
    private Long topicId;
    private String topicName;
    private LocalDateTime createdAt;
}
