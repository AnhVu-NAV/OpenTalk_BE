package sba301.java.opentalk.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sba301.java.opentalk.entity.Salary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByRecipientIdAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(
            Long recipientId, LocalDate start, LocalDate end);

    @Query("SELECT s FROM Salary s WHERE (:recipientId IS NULL OR s.recipient.id = :recipientId) " +
            "AND s.periodStart >= :start AND s.periodEnd <= :end")
    Page<Salary> search(
            @Param("recipientId") Long recipientId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            Pageable pageable);

    Optional<Salary> findByRecipientIdAndPeriodStartAndPeriodEnd(Long recipientId, LocalDate periodStart, LocalDate periodEnd);


}
