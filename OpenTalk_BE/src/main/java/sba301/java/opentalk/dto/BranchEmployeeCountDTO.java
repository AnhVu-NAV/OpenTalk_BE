package sba301.java.opentalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchEmployeeCountDTO {
    private String branchName;
    private Long employeeCount;
}
