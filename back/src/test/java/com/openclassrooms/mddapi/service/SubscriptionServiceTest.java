package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private TopicMapper topicMapper;
    @InjectMocks
    private SubscriptionService subscriptionService;

    private Topic buildTopic() {
        return Topic.builder().id(1L).name("Java").build();
    }

    private User buildUser(HashSet<Topic> topics) {
        return User.builder().id(1L).subscribedTopics(topics).build();
    }

    @Test
    void subscribe_shouldAddTopicToSubscriptions() {
        Topic topic = buildTopic();
        HashSet<Topic> topics = new HashSet<>();
        User user = buildUser(topics);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        subscriptionService.subscribe(user, 1L);

        assertThat(user.getSubscribedTopics()).contains(topic);
        verify(userRepository).save(user);
    }

    @Test
    void subscribe_shouldThrowWhenAlreadySubscribed() {
        Topic topic = buildTopic();
        HashSet<Topic> topics = new HashSet<>();
        topics.add(topic);
        User user = buildUser(topics);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> subscriptionService.subscribe(user, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Already subscribed to this topic");
    }

    @Test
    void unsubscribe_shouldRemoveTopicFromSubscriptions() {
        Topic topic = buildTopic();
        HashSet<Topic> topics = new HashSet<>();
        topics.add(topic);
        User user = buildUser(topics);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        subscriptionService.unsubscribe(user, 1L);

        assertThat(user.getSubscribedTopics()).doesNotContain(topic);
        verify(userRepository).save(user);
    }

    @Test
    void unsubscribe_shouldThrowWhenNotSubscribed() {
        Topic topic = buildTopic();
        User user = buildUser(new HashSet<>());

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> subscriptionService.unsubscribe(user, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not subscribed to this topic");
    }
}
