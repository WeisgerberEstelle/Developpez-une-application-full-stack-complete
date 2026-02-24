package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.AuthorResponse;
import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    AuthorResponse toAuthorResponse(User user);
}
