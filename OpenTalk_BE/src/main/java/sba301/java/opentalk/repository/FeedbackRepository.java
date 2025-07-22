package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.Feedback;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByMeetingId(Long meetingId);

    boolean existsByUserIdAndMeetingId(Long userId, Long meetingId);
}
