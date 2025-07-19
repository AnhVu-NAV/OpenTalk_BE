package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sba301.java.opentalk.enums.HostRegistrationStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class HostRegistrationDTO extends BaseDTO {
    private UserDTO user;
    private OpenTalkMeetingDTO meeting;
    private HostRegistrationStatus status;
}
