package sba301.java.opentalk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends BaseDTO {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private Boolean isEnabled;
    private String avatarUrl;
    private CompanyBranchDTO companyBranch;
    private long role;
}
