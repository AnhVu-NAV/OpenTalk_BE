package sba301.java.opentalk.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostFrequencyResponse {
    private Long userId;
    private String fullName;
    private String branchName;
    private Long approvedCount;
    private LocalDateTime lastApprovedAt;
}
