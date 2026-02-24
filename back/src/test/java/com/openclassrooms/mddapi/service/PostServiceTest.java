package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private PostMapper postMapper;
    @InjectMocks
    private PostService postService;

    private User buildUser(Set<Topic> topics) {
        return User.builder().id(1L).email("alice@example.com").subscribedTopics(topics).build();
    }

    @Test
    void getFeed_shouldReturnPostsFromSubscribedTopics() {
        Topic topic = Topic.builder().id(1L).name("Java").build();
        User user = buildUser(Set.of(topic));
        List<Post> posts = List.of(Post.builder().id(1L).title("Post 1").build());
        List<PostResponse> expected = List.of(new PostResponse());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findByTopicInOrderByCreatedAtDesc(user.getSubscribedTopics())).thenReturn(posts);
        when(postMapper.toResponseList(posts)).thenReturn(expected);

        List<PostResponse> result = postService.getFeed(user);

        assertThat(result).hasSize(1);
    }

    @Test
    void getFeed_shouldReturnEmptyListWhenNoSubscriptions() {
        User user = buildUser(new HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<PostResponse> result = postService.getFeed(user);

        assertThat(result).isEmpty();
        verify(postRepository, never()).findByTopicInOrderByCreatedAtDesc(any());
    }

    @Test
    void getById_shouldReturnMappedPost() {
        Post post = Post.builder().id(1L).title("Test").build();
        PostResponse expected = new PostResponse();
        expected.setTitle("Test");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toResponse(post)).thenReturn(expected);

        PostResponse result = postService.getById(1L);

        assertThat(result.getTitle()).isEqualTo("Test");
    }

    @Test
    void getById_shouldThrowWhenPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Post not found");
    }

    @Test
    void create_shouldSaveAndReturnMappedPost() {
        User user = buildUser(new HashSet<>());
        Topic topic = Topic.builder().id(1L).name("Java").build();
        CreatePostRequest request = new CreatePostRequest();
        request.setTopicId(1L);
        request.setTitle("New Post");
        request.setContent("Content");
        PostResponse expected = new PostResponse();

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postMapper.toResponse(any(Post.class))).thenReturn(expected);

        PostResponse result = postService.create(user, request);

        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
    }
}
