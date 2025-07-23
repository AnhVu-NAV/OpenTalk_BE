package sba301.java.opentalk.service;

import org.springframework.data.domain.Page;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.OpenTalkMeetingDetailDTO;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.enums.MeetingStatus;
import sba301.java.opentalk.model.request.OpenTalkCompletedRequest;
import sba301.java.opentalk.model.response.OpenTalkMeetingWithStatusDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OpenTalkMeetingService {
    OpenTalkMeetingDTO createMeeting(OpenTalkMeetingDTO topic);

    OpenTalkMeetingDTO updateMeeting(OpenTalkMeetingDTO topic, Long topicId);

    Page<OpenTalkMeetingDTO> getAllMeetings(String name, Long companyBranchId, MeetingStatus status, String dateStr, String fromDateStr, String toDateStr, int page, int size);

    List<OpenTalkMeetingDetailDTO> getOpenTalkMeetingWithDetails(String meetingName, Long branchId);

    List<OpenTalkMeetingDetailDTO> getOpenTalkMeetingWithDetailsForHost(String meetingName, Long branchId, String username);

    Page<OpenTalkMeetingDTO> getMeetingsCompleted(OpenTalkCompletedRequest request);

    Page<OpenTalkMeetingDTO> getRegisteredOpenTalks(Long userId, LocalDate startDate, LocalDate endDate, int page, int pageSize);

    Optional<OpenTalkMeetingDTO> getMeetingById(Long topicId);

    boolean deleteMeeting(Long topicId);

    boolean checkExistOpenTalk(LocalDateTime dateTime);

    void createScheduledOpenTalk();

    void sendMailRemind(Long openTalkId);

    OpenTalkMeetingDTO findMeetingById(long meetingId);

    OpenTalkMeetingDTO findMeetingByTopicId(long topicId);

    List<OpenTalkMeetingDTO> getMeetingsByCheckinCodesInRedis();

    List<OpenTalkMeetingWithStatusDTO> getRecentMeetingsWithStatusAttendance(Long userId, Long companyBranchId);

    void createEmptyOpenTalk();

    List<OpenTalkMeetingDTO> getAllMeetingsByStatus(MeetingStatus status);

    void scheduleMeetingStatusUpdate(OpenTalkMeeting openTalkMeeting);

    void updateMeetingStatus(Long meetingId, String jobType);
}
