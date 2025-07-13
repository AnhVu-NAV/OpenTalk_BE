package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sba301.java.opentalk.enums.HostRegistrationStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostRegistrationDTO extends BaseDTO {
    private long userId;
    private long openTalkMeetingId;
    private HostRegistrationStatus status;
}
