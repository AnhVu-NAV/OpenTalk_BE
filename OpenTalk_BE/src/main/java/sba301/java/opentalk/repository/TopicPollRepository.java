package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.TopicPoll;

@Repository
public interface TopicPollRepository extends JpaRepository<TopicPoll, Long> {
}
