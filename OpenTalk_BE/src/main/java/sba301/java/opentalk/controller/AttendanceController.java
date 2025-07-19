package sba301.java.opentalk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.enums.CheckinStatus;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.request.CheckinRequest;
import sba301.java.opentalk.model.response.CheckinCodeGenerateResponse;
import sba301.java.opentalk.service.AttendanceService;

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
}
