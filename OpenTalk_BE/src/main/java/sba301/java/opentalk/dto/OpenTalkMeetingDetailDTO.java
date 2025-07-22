package sba301.java.opentalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpenTalkMeetingDetailDTO extends BaseDTO {
    private String meetingName;
    private LocalDateTime scheduledDate;
    private String meetingLink;
    private String status;
    private TopicDTO topic;
    private UserDTO host;
    private List<Long> registeredHostUserIds;
    private CompanyBranchDTO companyBranch;
    private Integer avgRating;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TopicDTO {
        private String title;
        private String description;
        private String remark;
        private UserDTO suggestBy;
        private UserDTO evaluteBy;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserDTO {
        private String avatarUrl;
        private String username;
        private String email;
        private String fullName;
        private CompanyBranchDTO companyBranch;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CompanyBranchDTO {
        private String name;
    }
}
