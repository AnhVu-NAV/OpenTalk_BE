package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.PollDTO;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.TopicPollDTO;
import java.util.List;

public interface TopicPollService {
    public void createTopicPoll(long topicId, long pollId);
    public TopicPollDTO addTopicOption(TopicDTO topicPoll);
    public List<TopicPollDTO> getTopicPollByPoll(long openTalkMeetingId);
    public List<TopicPollDTO> getAll();
}
