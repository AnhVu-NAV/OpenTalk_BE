package sba301.java.opentalk.dto;

import lombok.*;
import sba301.java.opentalk.entity.Poll;
import sba301.java.opentalk.entity.Topic;

@Data
@EqualsAndHashCode(callSuper=true)
public class TopicPollDTO extends BaseDTO {
    private TopicDTO topic;
    private PollDTO poll;
}
