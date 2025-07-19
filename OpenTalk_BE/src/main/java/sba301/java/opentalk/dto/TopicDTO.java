package sba301.java.opentalk.dto;

import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
public class TopicDTO extends BaseDTO {
    private String title;
    private String description;
    private String status;
    private String remark;
    private UserDTO suggestedBy;
    private UserDTO evalutedBy;
}
