package sba301.java.opentalk.repository;

import sba301.java.opentalk.entity.OpenTalkTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OpenTalkTopicRepository extends JpaRepository<OpenTalkTopic, Long> {
    @Query("SELECT o FROM OpenTalkTopic o " +
            "JOIN o.companyBranch cb " +
            "JOIN OpenTalkRegistration r ON r.openTalkTopic.id = o.id " +
            "JOIN r.user u " +
            "WHERE (:companyBranchId IS NULL OR cb.id = :companyBranchId) " +
            "AND (:hostName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :hostName, '%'))) " +
            "AND (:isOrganized IS NULL OR o.isEnabled = :isOrganized) " +
            "AND (:isEnableOfHost IS NULL OR u.isEnabled = :isEnableOfHost) " +
            "AND (:startDate IS NULL OR o.scheduledDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.scheduledDate <= :endDate)")
    Page<OpenTalkTopic> findCompletedOpenTalks(@Param("companyBranchId") Long companyBranchId,
                                               @Param("hostName") String hostName,
                                               @Param("isOrganized") Boolean isOrganized,
                                               @Param("isEnableOfHost") Boolean isEnableOfHost,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate,
                                               Pageable pageable);

    @Query("SELECT o FROM OpenTalkTopic o " +
            "JOIN OpenTalkRegistration r ON r.openTalkTopic.id = o.id " +
            "WHERE r.user.id = :userId " +
            "AND (:startDate IS NULL OR o.scheduledDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.scheduledDate <= :endDate)")
    Page<OpenTalkTopic> findRegisteredOpenTalksByUser(@Param("userId") Long userId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate,
                                                      Pageable pageable);

    @Query("SELECT o FROM OpenTalkTopic o WHERE o.scheduledDate = :scheduledDate")
    Optional<OpenTalkTopic> findByScheduledDate(@Param("scheduledDate") LocalDate scheduledDate);
}
