package sba301.java.opentalk.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sba301.java.opentalk.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsAttendanceByUserIdAndOpenTalkMeetingId(Long userId, Long openTalkMeetingId);

    @Query("SELECT COUNT(*) FROM Attendance a WHERE a.user.id = :userId")
    Integer countAttendanceByUserId(@Param("userId") Long userId);
}
