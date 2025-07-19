package sba301.java.opentalk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import sba301.java.opentalk.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.User;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t WHERE t.status LIKE %?1% and t.title LIKE %?2%")
    Page<Topic> findByStatusAndTitle(String status, Pageable pageable, String title);

    List<Topic> findBySuggestedBy(User user);
}
