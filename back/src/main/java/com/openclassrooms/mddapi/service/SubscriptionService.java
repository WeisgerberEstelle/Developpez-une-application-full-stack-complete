package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public void subscribe(User user, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (managedUser.getSubscribedTopics().contains(topic)) {
            throw new IllegalArgumentException("Already subscribed to this topic");
        }

        managedUser.getSubscribedTopics().add(topic);
        userRepository.save(managedUser);
    }

    @Transactional
    public void unsubscribe(User user, Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!managedUser.getSubscribedTopics().contains(topic)) {
            throw new IllegalArgumentException("Not subscribed to this topic");
        }

        managedUser.getSubscribedTopics().remove(topic);
        userRepository.save(managedUser);
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> getSubscribedTopics(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return topicMapper.toResponseList(List.copyOf(managedUser.getSubscribedTopics()));
    }
}
