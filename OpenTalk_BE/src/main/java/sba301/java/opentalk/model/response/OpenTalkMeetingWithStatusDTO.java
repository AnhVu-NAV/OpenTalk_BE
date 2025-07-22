package sba301.java.opentalk.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import sba301.java.opentalk.entity.OpenTalkMeeting;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OpenTalkMeetingWithStatusDTO {
    private Long id;
    private String title;
    private LocalDateTime scheduledDate;
    private String time;
    private boolean attended;

    public OpenTalkMeetingWithStatusDTO(OpenTalkMeeting meeting, boolean attended) {
        this.id = meeting.getId();
        this.title = meeting.getMeetingName();
        this.scheduledDate = meeting.getScheduledDate();
        this.time = meeting.getScheduledDate().toLocalTime().toString();
        this.attended = attended;
    }
}

