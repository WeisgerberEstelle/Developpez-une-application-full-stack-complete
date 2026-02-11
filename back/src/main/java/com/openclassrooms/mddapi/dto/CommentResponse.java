package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private Long authorId;
    private String authorUsername;
    private LocalDateTime createdAt;
}
