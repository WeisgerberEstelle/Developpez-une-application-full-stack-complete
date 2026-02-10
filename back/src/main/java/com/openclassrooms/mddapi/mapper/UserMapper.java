package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserResponse;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "subscriptions", ignore = true)
    UserResponse toResponse(User user);
}
