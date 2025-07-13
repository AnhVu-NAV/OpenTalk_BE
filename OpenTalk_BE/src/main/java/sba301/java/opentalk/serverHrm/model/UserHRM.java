package sba301.java.opentalk.serverHrm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHRM {
    private String fullName;
    private String email;
    private String username;
    private Boolean isEnabled;
    private Long role;
    private Long companyBranch;
}
