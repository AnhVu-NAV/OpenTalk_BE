package sba301.java.opentalk.dto;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpenTalkTopicDTO extends BaseDTO {
    private String topicName;
    private long companyBranch;
    private LocalDate scheduledDate;
    private String meetingLink;
    private boolean isEnabled;
}
