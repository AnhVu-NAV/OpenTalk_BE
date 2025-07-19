package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.TopicVote;

public interface TopicVoteRepository extends JpaRepository<TopicVote, Long> {
     long countByTopicPoll(TopicPoll topicPoll);
}
