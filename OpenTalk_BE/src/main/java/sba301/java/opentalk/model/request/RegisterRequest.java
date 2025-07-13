package sba301.java.opentalk.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

import sba301.java.opentalk.validator.PasswordConstraint;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Fullname cannot be blank")
    @Size(min = 2, max = 50, message = "Fullname must be between 2 and 50 characters")
    private String fullname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @PasswordConstraint
    private String password;

    private Long companyBranchId;
}
