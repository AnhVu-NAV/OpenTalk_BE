package sba301.java.opentalk.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SalaryResponse {
    private Long id;
    private String recipientName;
    private String recipientEmail;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Integer totalHostSessions;
    private Integer totalAttendanceSessions;
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal totalSalary;
    private String formulaUsed;
    private String status;}
