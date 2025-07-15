package sba301.java.opentalk.dto;
import lombok.*;
import sba301.java.opentalk.entity.OpenTalkMeeting;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollDTO extends BaseDTO {
    private OpenTalkMeetingDTO openTalkMeetingDTO;
    private boolean isEnabled;
}
