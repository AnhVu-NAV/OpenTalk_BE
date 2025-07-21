package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.TopicVoteDTO;
import sba301.java.opentalk.model.response.TopicVoteResultResponse;

import java.util.List;

public interface TopicVoteService {
    TopicVoteDTO getTopicVote(long topicPollId);
    TopicVoteDTO saveTopicVote(TopicVoteDTO topicVote);
    List<TopicVoteResultResponse> getResultPoll(long PollId);
}
