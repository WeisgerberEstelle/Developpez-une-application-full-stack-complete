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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Service for post creation and feed retrieval.
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    /**
     * Returns the posts feed for the authenticated user based on subscribed topics.
     * Re-fetches the user to get a managed JPA entity with current subscriptions.
     * Skips the DB query if the user has no subscriptions.
     *
     * @param user the authenticated user
     * @return list of posts from subscribed topics, ordered by newest first
     */
    public List<PostResponse> getFeed(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (managedUser.getSubscribedTopics().isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> posts = postRepository.findByTopicInOrderByCreatedAtDesc(
                managedUser.getSubscribedTopics());
        return postMapper.toResponseList(posts);
    }

    @Transactional(readOnly = true)
    public PostResponse getById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return postMapper.toResponse(post);
    }

    @Transactional
    public PostResponse create(User user, CreatePostRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(managedUser)
                .topic(topic)
                .build();

        postRepository.save(post);
        return postMapper.toResponse(post);
    }
}
