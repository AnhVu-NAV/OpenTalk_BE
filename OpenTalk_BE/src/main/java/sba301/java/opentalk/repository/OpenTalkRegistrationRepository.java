package sba301.java.opentalk.repository;

import sba301.java.opentalk.dto.IOpenTalkRegistration;
import sba301.java.opentalk.entity.OpenTalkRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenTalkRegistrationRepository extends JpaRepository<OpenTalkRegistration, Long> {
    boolean existsByUserIdAndOpenTalkTopicId(Long userId, Long openTalkTopicId);

    List<OpenTalkRegistration> findByOpenTalkTopicId(Long topicId);

    @Query(value = "SELECT r.id AS id, r.created_at AS createdAt, r.updated_at AS updatedAt, " +
            "r.user_id AS userId, r.opentalk_topic_id AS openTalkTopicId, r.status AS status " +
            "FROM opentalk_registration r WHERE r.opentalk_topic_id = :topicId", nativeQuery = true)
    List<IOpenTalkRegistration> findRegistrationsByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT r FROM OpenTalkRegistration r " +
            "JOIN r.openTalkTopic t " +
            "JOIN r.user u " +
            "JOIN r.user.role ro " +
            "WHERE r.openTalkTopic.id = :topicId")
    List<OpenTalkRegistration> findByOpenTalkTopicIdWithNativeQuery(@Param("topicId") Long topicId);

//    @Query(value = "SELECT new ncc.java.opentalk.dto.OpenTalkRegistrationDTO() FROM opentalk_registration WHERE opentalk_topic_id = :topicId", nativeQuery = true)
//    List<OpenTalkRegistrationDTO> findByOpenTalkTopicIdWithNativeQuery(@Param("topicId") Long topicId);
    //@Param("topicId")
    //@Param("topicId")
}
