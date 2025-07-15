package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.TopicPollDTO;

public interface TopicPollService {
    public void createTopicPoll(TopicPollDTO topicPollDTO);

    public TopicPollDTO addTopicOption(TopicDTO topicPoll);

}
