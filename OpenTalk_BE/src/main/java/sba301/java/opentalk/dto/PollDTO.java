package sba301.java.opentalk.dto;
import lombok.*;
import sba301.java.opentalk.entity.OpenTalkMeeting;


@Data
@EqualsAndHashCode(callSuper=true)
public class PollDTO extends BaseDTO {
    private OpenTalkMeetingDTO openTalkMeeting;
    private boolean isEnabled;
}
