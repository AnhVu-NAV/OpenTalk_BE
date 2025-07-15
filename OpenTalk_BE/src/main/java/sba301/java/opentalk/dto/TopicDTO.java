package sba301.java.opentalk.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDTO extends BaseDTO {
    private String title;
    private String description;
    private String status;
    private String remark;
    private long suggestBy;   // toàn bộ User gợi ý
    private long evaluteBy;   // người duyệt
}
