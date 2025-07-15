package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDTO extends BaseDTO {
    private String title;
    private String description;
    private String status;
    private String remark;
    private UserDTO suggestedBy;
    private UserDTO evalutedBy;
}
