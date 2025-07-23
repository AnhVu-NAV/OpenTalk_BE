package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.dto.AttendanceSummaryDTO;
import sba301.java.opentalk.dto.UserAttendanceDTO;
import sba301.java.opentalk.entity.Attendance;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.enums.CheckinStatus;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.exception.ErrorCode;
import sba301.java.opentalk.model.request.CheckinRequest;
import sba301.java.opentalk.model.response.CheckinCodeGenerateResponse;
import sba301.java.opentalk.repository.AttendanceRepository;
import sba301.java.opentalk.repository.OpenTalkMeetingRepository;
import sba301.java.opentalk.repository.UserRepository;
import sba301.java.opentalk.service.AttendanceService;
import sba301.java.opentalk.service.RedisService;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final OpenTalkMeetingRepository openTalkMeetingRepository;
    private final RedisService redisService;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    private static final SecureRandom random = new SecureRandom();

    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CHECKIN_CODE = "checkin_code";

    @Override
    public boolean isCheckin(Long meetingId, Long userId) {
        return attendanceRepository.existsAttendanceByUserIdAndOpenTalkMeetingId(userId, meetingId);
    }

    @Override
    public CheckinCodeGenerateResponse generateCheckinCode(Long meetingId, int validMinutes) {
        String code = generateRandomCode();
        String redisKey = CHECKIN_CODE + code;

        redisService.saveKeyWithTTL(redisKey, String.valueOf(meetingId), validMinutes * 60L);
        return CheckinCodeGenerateResponse.builder()
                .checkinCode(code)
                .expiresAt(LocalDateTime.now().plusMinutes(validMinutes))
                .build();
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            sb.append(CODE_CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public String submitCheckin(CheckinRequest checkinRequest) throws AppException {
        String redisKey = CHECKIN_CODE + checkinRequest.getCheckinCode();
        String meetingIdStr = redisService.get(redisKey);

        if (meetingIdStr == null) {
            throw new AppException(ErrorCode.INVALID_OR_EXPIRED_CODE);
        }

        Long meetingId = Long.valueOf(meetingIdStr);

        boolean alreadyCheckedIn = attendanceRepository.existsAttendanceByUserIdAndOpenTalkMeetingId(checkinRequest.getUserId(), meetingId);
        if (alreadyCheckedIn) {
            throw new AppException(ErrorCode.ALREADY_CHECKED_IN);
        }

        User user = userRepository.findById(checkinRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        OpenTalkMeeting meeting = openTalkMeetingRepository.findById(Long.valueOf(meetingIdStr))
                .orElseThrow(() -> new AppException(ErrorCode.MEETING_NOT_FOUND));

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setOpenTalkMeeting(meeting);
        attendanceRepository.save(attendance);

        return CheckinStatus.SUCCESS.toString();
    }

    @Override
    public CheckinCodeGenerateResponse getCheckinCode(Long meetingId) {
        List<String> keys = redisService.getKeysByPattern("checkin_code*");
        for (String key : keys) {
            String value = redisService.get(key);
            long ttlInSeconds = redisService.getRemainingTtl(key);
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(ttlInSeconds);
            if (String.valueOf(meetingId).equals(value)) {
                return new CheckinCodeGenerateResponse(key.replace("checkin_code:", ""), expiresAt);
            }
        }
        return null;
    }

    @Override
    public Integer countAttendanceByUserIdAndValidTime(Long userId, LocalDate dateFrom, LocalDate dateTo) {
        LocalDateTime start = dateFrom.atStartOfDay();
        LocalDateTime end = dateTo.plusDays(1).atStartOfDay();
        return attendanceRepository.countAttendanceByUserIdAndCreatedAtBetween(userId, start, end);
    }

    @Override
    public List<AttendanceSummaryDTO> getTodayAttendanceSummary() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().plusDays(1).atStartOfDay();

        List<Attendance> attendances = attendanceRepository.findAllByCreatedAtBetween(start, end);

        return attendances.stream().map(att -> {
            User user = att.getUser();
            String checkinTime = att.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
            String status = att.getCreatedAt().toLocalTime().isAfter(LocalTime.of(9, 30)) ? "Late" : "On Time";
            String type = (user.getCompanyBranch() == null) ? "Remote" : "Office";

            return AttendanceSummaryDTO.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .avatarUrl(user.getAvatarUrl())
                    .role(user.getRole().getName())
                    .checkinTime(checkinTime)
                    .status(status)
                    .type(type)
                    .build();
        }).toList();
    }

    @Override
    public List<UserAttendanceDTO> getUserAttendance(Long userId) {
        List<Attendance> attendances = attendanceRepository.findAllByUserIdOrderByCreatedAtAsc(userId);

        List<UserAttendanceDTO> result = new ArrayList<>();

        for (Attendance attendance : attendances) {
            LocalDateTime checkInTime = attendance.getCreatedAt(); // Thời gian điểm danh (đã có sẵn)
            LocalTime standardCheckIn = LocalTime.of(9, 30);
            LocalTime fakeCheckout = LocalTime.of(19, 0); // Giả định check-out 07:00 PM
            Duration breakDuration = Duration.ofMinutes(30); // Giả định 30 phút nghỉ
            Duration workingDuration = Duration.between(checkInTime.toLocalTime(), fakeCheckout).minus(breakDuration);

            String status = checkInTime.toLocalTime().isAfter(standardCheckIn) ? "Late" : "On Time";

            UserAttendanceDTO dto = UserAttendanceDTO.builder()
                    .date(checkInTime.toLocalDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
                    .checkIn(checkInTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                    .checkOut(fakeCheckout.format(DateTimeFormatter.ofPattern("hh:mm a")))
                    .breakDuration(String.format("%02d:%02d Min", breakDuration.toHoursPart(), breakDuration.toMinutesPart()))
                    .workingHours(String.format("%02d:%02d Hrs", workingDuration.toHoursPart(), workingDuration.toMinutesPart()))
                    .status(status)
                    .build();

            result.add(dto);
        }

        return result;
    }

}
