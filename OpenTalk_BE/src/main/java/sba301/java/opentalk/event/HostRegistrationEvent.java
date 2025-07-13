package sba301.java.opentalk.event;

import lombok.Getter;
import sba301.java.opentalk.dto.OpenTalkMeetingDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.enums.HostRegistrationStatus;
import org.springframework.context.ApplicationEvent;

@Getter
public class HostRegistrationEvent extends ApplicationEvent {
    private final UserDTO user;
    private final OpenTalkMeetingDTO topic;
    private final HostRegistrationStatus status;

    public HostRegistrationEvent(Object source, UserDTO user, OpenTalkMeetingDTO topic, HostRegistrationStatus status) {
        super(source);
        this.user = user;
        this.topic = topic;
        this.status = status;
    }
}