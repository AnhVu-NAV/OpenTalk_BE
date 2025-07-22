package sba301.java.opentalk.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.Feedback;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByMeetingId(Long meetingId);

    boolean existsByUserIdAndMeetingId(Long userId, Long meetingId);


    @Query("SELECT AVG(f.rate) FROM Feedback f WHERE f.meeting.id = :meetingId")
    Double calculateAverageRateByMeetingId(@Param("meetingId") Long meetingId);
}
