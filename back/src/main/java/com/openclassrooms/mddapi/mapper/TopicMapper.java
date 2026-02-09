package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.entity.Topic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicResponse toResponse(Topic topic);
    List<TopicResponse> toResponseList(List<Topic> topics);
}
