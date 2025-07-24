package sba301.java.opentalk.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.dto.IHostRegistration;
import sba301.java.opentalk.dto.UserHostFrequency;
import sba301.java.opentalk.entity.HostRegistration;
import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.model.response.HostFrequencyResponse;
import sba301.java.opentalk.enums.HostRegistrationStatus;

import java.util.List;

@Repository
public interface HostRegistrationRepository extends JpaRepository<HostRegistration, Long> {
    boolean existsByUserIdAndOpenTalkMeetingId(Long userId, Long openTalkMeetingId);

    List<HostRegistration> findByOpenTalkMeetingIdAndStatus(Long openTalkMeetingId, HostRegistrationStatus status);


    @Query("SELECT r.user AS host, r.openTalkMeeting.id AS meetingId FROM HostRegistration r " +
            "WHERE r.openTalkMeeting.id IN :meetingIds AND r.status = sba301.java.opentalk.enums.HostRegistrationStatus.APPROVED")
    List<Tuple> findHostByOpenTalkMeetingIds(List<Long> meetingIds);

    @Query(value = "SELECT r.id AS id, r.created_at AS createdAt, r.updated_at AS updatedAt, " +
            "r.user_id AS userId, r.opentalk_meeting_id AS openTalkMeetingId, r.status AS status " +
            "FROM host_registration r WHERE r.opentalk_meeting_id = :topicId", nativeQuery = true)
    List<IHostRegistration> findRegistrationsByTopicId(@Param("topicId") Long topicId);

    @Query("SELECT r.user.id AS userId, r.openTalkMeeting.id AS openTalkMeetingId " +
            "FROM HostRegistration r WHERE r.openTalkMeeting.id IN :meetingIds")
    List<Tuple> findUserIdAndOpenTalkMeetingIdsByOpenTalkMeetingId(List<Long> meetingIds);

    @Query("SELECT r FROM HostRegistration r " +
            "JOIN r.openTalkMeeting t " +
            "JOIN r.user u " +
            "JOIN r.user.role ro " +
            "WHERE r.openTalkMeeting.id = :topicId")
    List<HostRegistration> findByOpenTalkMeetingIdWithNativeQuery(@Param("topicId") Long topicId);

    @Query(value = """
            SELECT  u.id AS userId,
                    u.full_name AS fullName,
                    c.name AS branchName,
                    SUM(CASE WHEN t.status = 'APPROVED' THEN 1 ELSE 0 END) AS approvedCount,
                    MAX(CASE WHEN t.status = 'APPROVED' THEN t.created_at END) AS lastApprovedAt
            FROM `user` u
            LEFT JOIN host_registration t ON t.user_id = u.id
            LEFT JOIN company_branch c ON u.company_branch_id = c.id
            GROUP BY u.id, u.full_name, c.name
            """, nativeQuery = true)
    List<UserHostFrequency> getUserHostFrequency();

    @Query("SELECT r.openTalkMeeting.id, COUNT(r) FROM HostRegistration r " +
            "WHERE r.openTalkMeeting.id  IN :meetingIds AND r.status = :status GROUP BY r.openTalkMeeting.id")
    List<Object[]> countRequestsByMeetingIds(@Param("meetingIds") List<Long> meetingIds,
                                             @Param("status") HostRegistrationStatus status);
}
