package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private AuthorResponse author;
    private TopicResponse topic;
    private LocalDateTime createdAt;
}
