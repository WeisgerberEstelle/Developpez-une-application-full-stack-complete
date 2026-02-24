package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private AuthorResponse author;
    private LocalDateTime createdAt;
}
