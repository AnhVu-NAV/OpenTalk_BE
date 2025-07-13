package sba301.java.opentalk.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpenTalkMeetingDTO extends BaseDTO {
    private String topicName;
    private long companyBranch;
    private LocalDate scheduledDate;
    private String meetingLink;
    private boolean isEnabled;
}
