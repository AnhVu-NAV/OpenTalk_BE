package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackDTO extends BaseDTO {
    private Integer rate;
    private String comment;
    private UserDTO user;
    private OpenTalkMeetingDTO meeting;
}