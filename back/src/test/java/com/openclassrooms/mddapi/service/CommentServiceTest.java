package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentResponse;
import com.openclassrooms.mddapi.dto.CreateCommentRequest;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentService commentService;

    @Test
    void getByPostId_shouldReturnMappedComments() {
        Post post = Post.builder().id(1L).build();
        List<Comment> comments = List.of(Comment.builder().id(1L).content("Hello").build());
        List<CommentResponse> expected = List.of(new CommentResponse());

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(1L)).thenReturn(comments);
        when(commentMapper.toResponseList(comments)).thenReturn(expected);

        List<CommentResponse> result = commentService.getByPostId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getByPostId_shouldThrowWhenPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getByPostId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Post not found");
    }

    @Test
    void create_shouldSaveAndReturnMappedComment() {
        User user = User.builder().id(1L).build();
        Post post = Post.builder().id(1L).build();
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Nice post");
        CommentResponse expected = new CommentResponse();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentMapper.toResponse(any(Comment.class))).thenReturn(expected);

        CommentResponse result = commentService.create(user, 1L, request);

        assertThat(result).isEqualTo(expected);
        verify(commentRepository).save(any(Comment.class));
    }
}
