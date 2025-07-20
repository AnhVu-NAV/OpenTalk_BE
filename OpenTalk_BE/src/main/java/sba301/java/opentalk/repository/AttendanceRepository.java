package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sba301.java.opentalk.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsAttendanceByUserIdAndOpenTalkMeetingId(Long userId, Long openTalkMeetingId);
}
