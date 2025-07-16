package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.User;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    public Optional<TopicDTO> getTopic(long id);
    public List<TopicDTO> getAllTopics();
    public TopicDTO addTopic(TopicDTO topic);
    public TopicDTO updateTopic(TopicDTO topic);
    public TopicDTO deleteTopic(long id);
    public List<TopicDTO> getTopics(int pageNo, int pageSize, String status);
    public List<TopicDTO> getTopicsByUser(long userId);
}
