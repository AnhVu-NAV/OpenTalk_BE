package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicVoteDTO {
    private UserDTO voter;
    private TopicPollDTO topicPoll;
}
