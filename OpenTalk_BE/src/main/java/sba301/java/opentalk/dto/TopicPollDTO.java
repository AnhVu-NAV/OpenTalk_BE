package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicPollDTO extends BaseDTO {
    private long topic;
    private long poll;
}
