package sba301.java.opentalk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salary")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Salary extends BaseEntity {
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "total_host_sessions", nullable = false)
    private Integer totalHostSessions;

    @Column(name = "total_attendance_sessions", nullable = false)
    private Integer totalAttendanceSessions;

    @Column(name = "base_salary", precision = 12, scale = 2, nullable = false)
    private BigDecimal baseSalary;

    @Column(name = "bonus", precision = 12, scale = 2, nullable = false)
    private BigDecimal bonus;

    @Column(name = "total_salary", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalSalary;

    @Column(name = "formula_used", columnDefinition = "TEXT")
    private String formulaUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;
}
