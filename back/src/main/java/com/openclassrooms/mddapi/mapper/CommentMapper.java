package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.username", target = "authorUsername")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
