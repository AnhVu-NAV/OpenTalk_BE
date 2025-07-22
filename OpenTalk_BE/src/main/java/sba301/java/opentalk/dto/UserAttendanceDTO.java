package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAttendanceDTO {
    private String date; // e.g. July 01, 2023
    private String checkIn; // 09:28 AM
    private String checkOut;
    private String breakDuration;
    private String workingHours;
    private String status;
}
