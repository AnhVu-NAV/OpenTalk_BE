package sba301.java.opentalk.repository;

import sba301.java.opentalk.entity.User;
import sba301.java.opentalk.serverHrm.model.UserFromHRM;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM user u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsernameNative(@Param("username") String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE (:companyBranchId IS NULL OR u.companyBranch.id = :companyBranchId) " +
            "AND (:employeeName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
            "AND (:isEnabled IS NULL OR u.isEnabled = :isEnabled) " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 FROM HostRegistration r " +
            "    JOIN r.openTalkMeeting o " +
            "    WHERE r.user.id = u.id " +
            "    AND (:startDate IS NULL OR o.scheduledDate >= :startDate) " +
            "    AND (:endDate IS NULL OR o.scheduledDate <= :endDate) " +
            ")")
    Slice<User> getUnregisteredEmployees(@Param("companyBranchId") Long companyBranchId,
                                         @Param("employeeName") String employeeName,
                                         @Param("isEnabled") Boolean isEnabled,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    List<User> findAllByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u FROM User u WHERE u.isEnabled = true")
    List<User> findEligibleUsers();

    @Query(value = "SELECT full_name AS fullName, " +
            "email AS email, " +
            "username AS username, " +
            "is_enabled AS isEnabled, " +
            "role_id AS role, " +
            "company_branch_id AS companyBranch " +
            "FROM user", nativeQuery = true)
    List<UserFromHRM> findAllUsersFormHRM();
}