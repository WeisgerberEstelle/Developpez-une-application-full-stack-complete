package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TopicMapper.class)
public interface UserMapper {
    @Mapping(source = "subscribedTopics", target = "subscriptions")
    UserResponse toResponse(User user);
}
