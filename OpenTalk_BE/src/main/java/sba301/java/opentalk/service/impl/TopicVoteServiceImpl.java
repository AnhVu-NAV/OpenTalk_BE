package sba301.java.opentalk.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.TopicVoteDTO;
import sba301.java.opentalk.entity.Poll;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.TopicVote;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.model.response.TopicVoteResultResponse;
import sba301.java.opentalk.repository.*;
import sba301.java.opentalk.service.TopicVoteService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicVoteServiceImpl implements TopicVoteService {
    private final TopicVoteRepository topicVoteRepository;
    private final TopicPollRepository topicPollRepository;
    private final UserRepository userRepository;
    private final PollRepository pollRepository;

    @Override
    public TopicVoteDTO getTopicVote(long topicPollId) {
        return null;
    }

    @Override
    public TopicVoteDTO saveTopicVote(TopicVoteDTO topicVote) {
        TopicPoll topicPoll = topicPollRepository.findById(topicVote.getTopicPollId()).orElse(null);
        User user = userRepository.findById(topicVote.getVoter().getId()).orElse(null);
        if (topicPoll == null) {
            throw new IllegalArgumentException("Topic Poll Not Found");
        }
        TopicVote topicVoteEntity = new TopicVote();
        topicVoteEntity.setTopicPoll(topicPoll);
        topicVoteEntity.setVoter(user);
        topicVoteRepository.save(topicVoteEntity);
        return topicVote;
    }

    @Override
    public List<TopicVoteResultResponse> getResultPoll(long pollId) {
        List<TopicVoteResultResponse> resultPoll = new ArrayList<>();
        Poll poll = pollRepository.findById((int)pollId).orElse(null);
        List<TopicPoll> topicPollList = topicPollRepository.findByPoll(poll);
        for(TopicPoll topicPoll : topicPollList) {
            TopicVoteResultResponse resultPollResponse = new TopicVoteResultResponse();
            resultPollResponse.setTopicPollId(topicPoll.getId());
            resultPollResponse.setResult(topicVoteRepository.countByTopicPoll(topicPoll));
            resultPoll.add(resultPollResponse);
        }
        return resultPoll;
    }
}
