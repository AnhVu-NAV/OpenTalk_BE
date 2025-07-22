package sba301.java.opentalk.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sba301.java.opentalk.entity.Attendance;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsAttendanceByUserIdAndOpenTalkMeetingId(Long userId, Long openTalkMeetingId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId AND a.createdAt >= :startDateTime AND a.createdAt < :endDateTime")
    Integer countAttendanceByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    List<Attendance> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Attendance> findAllByUserIdOrderByCreatedAtAsc(Long userId);
}
