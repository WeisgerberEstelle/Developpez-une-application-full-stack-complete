package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.AuthorResponse;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/** MapStruct mapper for converting Post entities to DTOs (includes nested author and topic). */
@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toResponse(Post post);
    List<PostResponse> toResponseList(List<Post> posts);

    AuthorResponse toAuthorResponse(User user);
    TopicResponse toTopicResponse(Topic topic);
}
