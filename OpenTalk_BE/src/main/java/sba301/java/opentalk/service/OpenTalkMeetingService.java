package sba301.java.opentalk.service;

import org.springframework.data.domain.Page;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDetailDTO;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpenTalkMeetingService {
    OpenTalkMeetingDTO createMeeting(OpenTalkMeetingDTO topic);

    OpenTalkMeetingDTO updateMeeting(OpenTalkMeetingDTO topic);

    List<OpenTalkMeetingDTO> getAllMeetings();

    List<OpenTalkMeetingDetailDTO> getOpenTalkMeetingWithDetails(String meetingName, Long branchId);

    Page<OpenTalkMeetingDTO> getMeetingsCompleted(OpenTalkCompletedRequest request);

    Page<OpenTalkMeetingDTO> getRegisteredOpenTalks(Long userId, LocalDate startDate, LocalDate endDate, int page, int pageSize);

    Optional<OpenTalkMeetingDTO> getMeetingById(Long topicId);

    boolean deleteMeeting(Long topicId);

    boolean checkExistOpenTalk(LocalDateTime dateTime);

    void createScheduledOpenTalk();

    void sendMailRemind(Long openTalkId);

    OpenTalkMeetingDTO findMeetingById(long meetingId);

    OpenTalkMeetingDTO findMeetingByTopicId(long topicId);
}
