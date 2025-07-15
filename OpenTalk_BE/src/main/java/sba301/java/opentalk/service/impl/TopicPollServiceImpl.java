package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.mapper.TopicPollMapper;
import sba301.java.opentalk.repository.TopicPollRepository;
import sba301.java.opentalk.service.TopicPollService;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicPollServiceImpl implements TopicPollService {
    private final TopicPollRepository topicPollRepository;
    @Override
    public void createTopicPoll(TopicPollDTO topicPollDTO) {
        topicPollRepository.save(TopicPollMapper.INSTANCE.toEntity(topicPollDTO));
    }

    @Override
    public TopicPollDTO addTopicOption(TopicDTO topicPoll) {
        return null;
    }
}
