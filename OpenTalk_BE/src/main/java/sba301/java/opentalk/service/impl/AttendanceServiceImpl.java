package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import java.time.LocalDateTime;

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
        String redisKey = "checkin_code" + checkinRequest.getCheckinCode();
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
}
