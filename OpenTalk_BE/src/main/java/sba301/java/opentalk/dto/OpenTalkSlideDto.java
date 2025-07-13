package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class OpenTalkSlideDto extends BaseDTO {
    private String fileName;
    private String filePath;
    private Long userId;
    private Long openTalkTopicId;
}
