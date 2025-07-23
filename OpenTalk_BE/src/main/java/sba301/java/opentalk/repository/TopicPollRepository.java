package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.TopicPoll;
import sba301.java.opentalk.entity.Poll;
import java.util.List;

@Repository
public interface TopicPollRepository extends JpaRepository<TopicPoll, Long> {
    List<TopicPoll>  findByPollId(Long id);
    List<TopicPoll> findByPoll(Poll poll);

}
