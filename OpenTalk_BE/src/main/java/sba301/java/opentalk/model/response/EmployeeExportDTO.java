package sba301.java.opentalk.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeExportDTO {
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String companyBranchName;
    private String roleName;
    private Boolean isEnabled;
}
