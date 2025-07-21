package sba301.java.opentalk.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicVoteResultResponse {
    private long topicPollId;
    private long result;
}
