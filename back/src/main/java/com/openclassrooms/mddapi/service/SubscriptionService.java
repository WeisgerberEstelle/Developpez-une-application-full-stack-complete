package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.DuplicateResourceException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @Transactional
    /**
     * Subscribes the user to a topic.
     *
     * @param user    the authenticated user
     * @param topicId the topic to subscribe to
     * @throws DuplicateResourceException if already subscribed
     * @throws ResourceNotFoundException if topic not found
     */
    public void subscribe(User user, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (managedUser.getSubscribedTopics().contains(topic)) {
            throw new DuplicateResourceException("Already subscribed to this topic");
        }

        managedUser.getSubscribedTopics().add(topic);
        userRepository.save(managedUser);
    }

    @Transactional
    /**
     * Unsubscribes the user from a topic.
     *
     * @param user    the authenticated user
     * @param topicId the topic to unsubscribe from
     * @throws ResourceNotFoundException if not subscribed or topic not found
     */
    public void unsubscribe(User user, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!managedUser.getSubscribedTopics().contains(topic)) {
            throw new ResourceNotFoundException("Not subscribed to this topic");
        }

        managedUser.getSubscribedTopics().remove(topic);
        userRepository.save(managedUser);
    }
}
