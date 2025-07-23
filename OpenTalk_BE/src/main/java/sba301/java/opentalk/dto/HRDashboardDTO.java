package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class HRDashboardDTO {
    private Long totalEmployees;
    private Long totalMeetings;
    private Double attendanceRate;
    private Map<String, Integer> monthlyAttendanceTrends;
    private Map<String, Integer> branchMeetingStats;
    List<OpenTalkMeetingDTO> recentMeetings;
}
