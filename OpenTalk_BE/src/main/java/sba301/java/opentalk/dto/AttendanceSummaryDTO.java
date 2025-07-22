package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceSummaryDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String role;
    private String checkinTime;
    private String type;
    private String status;
}
