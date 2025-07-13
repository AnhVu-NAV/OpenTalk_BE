package sba301.java.opentalk.event;

import lombok.Getter;
import sba301.java.opentalk.dto.OpenTalkTopicDTO;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.enums.OpenTalkRegistrationStatus;
import org.springframework.context.ApplicationEvent;

@Getter
public class OpenTalkRegistrationEvent extends ApplicationEvent {
    private final UserDTO user;
    private final OpenTalkTopicDTO topic;
    private final OpenTalkRegistrationStatus status;


    public OpenTalkRegistrationEvent(Object source, UserDTO user, OpenTalkTopicDTO topic, OpenTalkRegistrationStatus status) {
        super(source);
        this.user = user;
        this.topic = topic;
        this.status = status;
    }
}
