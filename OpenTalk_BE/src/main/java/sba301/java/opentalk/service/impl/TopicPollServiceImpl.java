package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.TopicPollDTO;
import sba301.java.opentalk.entity.Poll;
import sba301.java.opentalk.entity.Topic;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.mapper.TopicPollMapper;
import sba301.java.opentalk.repository.PollRepository;
import sba301.java.opentalk.repository.TopicPollRepository;
import sba301.java.opentalk.repository.TopicRepository;
import sba301.java.opentalk.service.OpenTalkMeetingService;
import sba301.java.opentalk.service.PollService;
import sba301.java.opentalk.service.TopicPollService;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class TopicPollServiceImpl implements TopicPollService {
    private final TopicPollRepository topicPollRepository;
    private final OpenTalkMeetingService openTalkMeetingService;
    private final PollRepository pollRepository;
    private final TopicRepository topicRepository;

    @Override
    public void createTopicPoll(long topicId, long pollId) {
        Poll poll = pollRepository.findById((int) pollId).get();
        Topic topic = topicRepository.findById(topicId).get();
        TopicPoll topicPoll = new TopicPoll();
        topicPoll.setTopic(topic);
        topicPoll.setPoll(poll);
        topicPollRepository.save(topicPoll);
    }

    @Override
    public TopicPollDTO addTopicOption(TopicDTO topicPoll) {
        return null;
    }

    @Override
    public List<TopicPollDTO> getTopicPollByPoll(long pollId) {
        return topicPollRepository.findByPollId(pollId).stream().map(TopicPollMapper.INSTANCE::toDto).collect(Collectors.toList());
    }


    @Override
    public List<TopicPollDTO> getAll() {
        return topicPollRepository.findAll().stream().map(tp -> TopicPollMapper.INSTANCE.toDto(tp)).collect(Collectors.toList());
    }
}
