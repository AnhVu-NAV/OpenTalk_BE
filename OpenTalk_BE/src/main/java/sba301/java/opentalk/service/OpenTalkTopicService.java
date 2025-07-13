package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.OpenTalkTopicDTO;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OpenTalkTopicService {
    OpenTalkTopicDTO createTopic(OpenTalkTopicDTO topic);

    OpenTalkTopicDTO updateTopic(OpenTalkTopicDTO topic);

    List<OpenTalkTopicDTO> getAllTopics();

    Page<OpenTalkTopicDTO> getTopicsCompleted(OpenTalkCompletedRequest request);

    Page<OpenTalkTopicDTO> getRegisteredOpenTalks(Long userId, LocalDate startDate, LocalDate endDate, int page, int pageSize);

    Optional<OpenTalkTopicDTO> getTopicById(Long topicId);

    boolean deleteTopic(Long topicId);

    public boolean checkExistOpenTalk(LocalDate date);

    public void createScheduledOpenTalk();

    public void sendMailRemind(Long openTalkId);
}
