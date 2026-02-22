package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.TopicResponse;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;
    @Mock
    private TopicMapper topicMapper;
    @InjectMocks
    private TopicService topicService;

    @Test
    void getAllTopics_shouldReturnMappedTopics() {
        List<Topic> topics = List.of(
                Topic.builder().id(1L).name("Java").build(),
                Topic.builder().id(2L).name("Angular").build()
        );
        List<TopicResponse> expected = List.of(new TopicResponse(), new TopicResponse());

        when(topicRepository.findAll()).thenReturn(topics);
        when(topicMapper.toResponseList(topics)).thenReturn(expected);

        List<TopicResponse> result = topicService.getAllTopics();

        assertThat(result).hasSize(2);
    }
}
