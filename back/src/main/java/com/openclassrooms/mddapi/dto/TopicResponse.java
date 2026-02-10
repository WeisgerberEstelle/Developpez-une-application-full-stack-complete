package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class TopicResponse {
    private Long id;
    private String name;
    private String description;
}
