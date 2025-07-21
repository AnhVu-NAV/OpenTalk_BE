package sba301.java.opentalk.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserDTO extends BaseDTO implements Serializable {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private Boolean isEnabled;
    private CompanyBranchDTO companyBranch;
    private Long role;
    private String avatarUrl;

    private Long roleId;
    private Long companyBranchId;

    private String roleName;
    private String companyBranchName;
}