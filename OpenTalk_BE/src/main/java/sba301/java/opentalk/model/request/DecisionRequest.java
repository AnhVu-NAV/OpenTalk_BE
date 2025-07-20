package sba301.java.opentalk.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecisionRequest {
    private long topicId;
    private String decision;
    private long userId;
    private String remark;//evaluteBy Id
}
