package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.Poll;
import java.util.List;

@Repository
public interface TopicPollRepository extends JpaRepository<TopicPoll, Long> {
    List<TopicPoll> findByPoll(Poll poll);
}
