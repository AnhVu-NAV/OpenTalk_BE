package sba301.java.opentalk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.OpenTalkMeeting;
import sba301.java.opentalk.enums.MeetingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpenTalkMeetingRepository extends JpaRepository<OpenTalkMeeting, Long> {
    @Query("SELECT o FROM OpenTalkMeeting o " +
            "JOIN o.companyBranch cb " +
            "JOIN HostRegistration r ON r.openTalkMeeting.id = o.id " +
            "JOIN r.user u " +
            "WHERE (:companyBranchId IS NULL OR cb.id = :companyBranchId) " +
            "AND (:hostName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :hostName, '%'))) " +
            "AND (:isOrganized IS NULL OR o.status = :isOrganized) " +
            "AND (:isEnableOfHost IS NULL OR u.isEnabled = :isEnableOfHost) " +
            "AND (:startDate IS NULL OR o.scheduledDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.scheduledDate <= :endDate)")
    Page<OpenTalkMeeting> findCompletedOpenTalks(@Param("companyBranchId") Long companyBranchId,
                                                 @Param("hostName") String hostName,
                                                 @Param("isOrganized") Boolean isOrganized,
                                                 @Param("isEnableOfHost") Boolean isEnableOfHost,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 Pageable pageable);

    @Query("SELECT o FROM OpenTalkMeeting o WHERE o.status IN :statuses " +
            "AND (:meetingName IS NULL OR LOWER(o.meetingName) LIKE LOWER(CONCAT('%', :meetingName, '%'))) " +
            "AND (:branchId IS NULL OR o.companyBranch.id = :branchId)")
    List<OpenTalkMeeting> findByStatusInWithMeetingNameAndBranchId(@Param("statuses") List<MeetingStatus> statuses,
                                                                   @Param("meetingName") String meetingName,
                                                                   @Param("branchId") Long branchId);

    @Query("SELECT o FROM OpenTalkMeeting o " +
            "JOIN HostRegistration r ON r.openTalkMeeting.id = o.id " +
            "WHERE r.user.id = :userId " +
            "AND (:startDate IS NULL OR o.scheduledDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.scheduledDate <= :endDate)")
    Page<OpenTalkMeeting> findHostRegisteredOpenTalksByUser(@Param("userId") Long userId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate,
                                                            Pageable pageable);

    @Query("SELECT o FROM OpenTalkMeeting o WHERE o.scheduledDate = :scheduledDate")
    Optional<OpenTalkMeeting> findByScheduledDate(@Param("scheduledDate") LocalDateTime scheduledDate);

    Optional<OpenTalkMeeting> findByTopicId(Long id);

    @Query("SELECT o FROM OpenTalkMeeting o " +
            "JOIN o.companyBranch cb " +
            "WHERE " +
            "(:name IS NULL OR LOWER(o.meetingName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:companyBranchId IS NULL OR cb.id = :companyBranchId) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND ( " +
            "   (:date IS NULL OR FUNCTION('DATE', o.scheduledDate) = :date) " +
            ") " +
            "AND (:fromDate IS NULL OR o.scheduledDate >= :fromDate) " +
            "AND (:toDate IS NULL OR o.scheduledDate <= :toDate) "
    )
    Page<OpenTalkMeeting> findWithFilter(@Param("name") String name,
                                         @Param("companyBranchId") Long companyBranchId,
                                         @Param("status") MeetingStatus status,
                                         @Param("date") LocalDate date,
                                         @Param("fromDate") LocalDateTime fromDate,
                                         @Param("toDate") LocalDateTime toDate,
                                         Pageable pageable);

    List<OpenTalkMeeting> findByCompanyBranchAndScheduledDateBetween(Long companyBranchId, LocalDateTime start, LocalDateTime end);

}
