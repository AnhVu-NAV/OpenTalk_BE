package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.AttendanceSummaryDTO;
import sba301.java.opentalk.dto.UserAttendanceDTO;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.request.CheckinRequest;
import sba301.java.opentalk.model.response.CheckinCodeGenerateResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    boolean isCheckin(Long meetingId, Long userId);

    CheckinCodeGenerateResponse generateCheckinCode(Long meetingId, int validMinutes);

    String submitCheckin(CheckinRequest checkinRequest) throws AppException;

    CheckinCodeGenerateResponse getCheckinCode(Long meetingId);

    Integer countAttendanceByUserIdAndValidTime(Long userId, LocalDate dateFrom, LocalDate dateTo);

    List<AttendanceSummaryDTO> getTodayAttendanceSummary();

    List<UserAttendanceDTO> getUserAttendance(Long userId);
}
