package sba301.java.opentalk.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVoteAbilityRequest {
    long userId;
    long pollId;
}
