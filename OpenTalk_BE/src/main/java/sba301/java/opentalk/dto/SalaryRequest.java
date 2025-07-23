package sba301.java.opentalk.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalaryRequest {
    private List<Long> recipientIds;
    private LocalDate periodStart;
    private LocalDate periodEnd;


}
