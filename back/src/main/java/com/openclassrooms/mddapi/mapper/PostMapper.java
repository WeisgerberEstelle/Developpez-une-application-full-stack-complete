package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.name", target = "topicName")
    PostResponse toResponse(Post post);
    List<PostResponse> toResponseList(List<Post> posts);
}
