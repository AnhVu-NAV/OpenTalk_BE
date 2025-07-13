package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sba301.java.opentalk.enums.OpenTalkRegistrationStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenTalkRegistrationDTO extends BaseDTO {
    private long userId;
    private long openTalkTopicId;
    private OpenTalkRegistrationStatus status;
}
