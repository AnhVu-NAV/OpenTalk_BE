package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sba301.java.opentalk.dto.AttendanceSummaryDTO;
import sba301.java.opentalk.dto.UserAttendanceDTO;
import sba301.java.opentalk.enums.CheckinStatus;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.request.CheckinRequest;
import sba301.java.opentalk.model.response.CheckinCodeGenerateResponse;
import sba301.java.opentalk.service.AttendanceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/generate-checkin-code")
    public ResponseEntity<CheckinCodeGenerateResponse> generateCheckinCode(
            @RequestParam Long meetingId,
            @RequestParam(defaultValue = "15") int validMinutes) {
        return ResponseEntity.ok(attendanceService.generateCheckinCode(meetingId, validMinutes));
    }

    @PostMapping("/checkin")
    public ResponseEntity<String> checkin(@RequestBody CheckinRequest checkinRequest) throws AppException {
        return ResponseEntity.ok(attendanceService.submitCheckin(checkinRequest));
    }

    @GetMapping("/checkin-status")
    public ResponseEntity<String> getCheckinStatus(
            @RequestParam Long meetingId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(attendanceService.isCheckin(meetingId, userId) ? CheckinStatus.ALREADY_CHECKED_IN.toString() : CheckinStatus.NOT_CHECKED_IN.toString());
    }

    @GetMapping("/checkin-code")
    public ResponseEntity<CheckinCodeGenerateResponse> getCheckinCode(@RequestParam Long meetingId) {
        return ResponseEntity.ok(attendanceService.getCheckinCode(meetingId));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<AttendanceSummaryDTO>> getTodayAttendanceSummary() {
        return ResponseEntity.ok(attendanceService.getTodayAttendanceSummary());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAttendanceDTO>> getUserAttendance(@PathVariable Long userId) {
        List<UserAttendanceDTO> records = attendanceService.getUserAttendance(userId);
        return ResponseEntity.ok(records);
    }
}
