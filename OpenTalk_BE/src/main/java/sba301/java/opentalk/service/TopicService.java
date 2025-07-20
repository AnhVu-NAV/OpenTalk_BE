package sba301.java.opentalk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sba301.java.opentalk.dto.TopicDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.TopicStatus;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    public Optional<TopicDTO> getTopic(long id);
    public Page<TopicDTO> getAllTopics(int page, int size);
    public TopicDTO addTopic(TopicDTO topic);
    public TopicDTO updateTopic(TopicDTO topic);
    public TopicDTO deleteTopic(long id);
    public List<TopicDTO> getTopicsByUser(long userId);
    Page<TopicDTO> findByStatusAndTitle(String status, Pageable pageable, String title);
    public TopicDTO evaluteTopic(long id, String decision, long userId, String remark);

}
