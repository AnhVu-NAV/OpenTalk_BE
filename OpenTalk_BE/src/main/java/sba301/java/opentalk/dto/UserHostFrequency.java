package sba301.java.opentalk.dto;

import java.time.LocalDateTime;

public interface UserHostFrequency {
    Long getUserId();
    String getFullName();
    String getBranchName();
    Long getApprovedCount();
    LocalDateTime getLastApprovedAt();
}
