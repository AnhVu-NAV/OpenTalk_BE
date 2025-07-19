package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.entity.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Integer> {
    Poll findByOpenTalkMeetingId(long meetingId);
}
