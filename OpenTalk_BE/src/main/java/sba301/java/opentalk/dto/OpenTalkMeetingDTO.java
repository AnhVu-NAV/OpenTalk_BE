package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sba301.java.opentalk.enums.MeetingStatus;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenTalkMeetingDTO extends BaseDTO {
    private String meetingName;
    private LocalDateTime scheduledDate;
    private String meetingLink;
    private MeetingStatus status;
    private CompanyBranchDTO companyBranch;
    private TopicDTO topic;
}
