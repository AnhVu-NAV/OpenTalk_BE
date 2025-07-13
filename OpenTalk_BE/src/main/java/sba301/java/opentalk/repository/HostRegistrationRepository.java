package sba301.java.opentalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.dto.IHostRegistration;
import sba301.java.opentalk.entity.HostRegistration;

import java.util.List;

@Repository
public interface HostRegistrationRepository extends JpaRepository<HostRegistration, Long> {
    boolean existsByUserIdAndOpenTalkMeetingId(Long userId, Long openTalkMeetingId);

    List<HostRegistration> findByOpenTalkMeetingId(Long topicId);

    @Query(value = "SELECT r.id AS id, r.created_at AS createdAt, r.updated_at AS updatedAt, " +
            "r.user_id AS userId, r.opentalk_meeting_id AS openTalkMeetingId, r.status AS status " +
            "FROM host_registration r WHERE r.opentalk_meeting_id = :topicId", nativeQuery = true)
    List<IHostRegistration> findRegistrationsByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT r FROM HostRegistration r " +
            "JOIN r.openTalkMeeting t " +
            "JOIN r.user u " +
            "JOIN r.user.role ro " +
            "WHERE r.openTalkMeeting.id = :topicId")
    List<HostRegistration> findByOpenTalkMeetingIdWithNativeQuery(@Param("topicId") Long topicId);
}
