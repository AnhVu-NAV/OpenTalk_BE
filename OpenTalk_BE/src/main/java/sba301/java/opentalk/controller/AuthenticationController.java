package sba301.java.opentalk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.ApiResponse;
import sba301.java.opentalk.model.request.AuthenticationRequest;
import sba301.java.opentalk.model.request.ForgotPasswordRequest;
import sba301.java.opentalk.model.request.RegisterRequest;
import sba301.java.opentalk.model.request.ResetPasswordRequest;
import sba301.java.opentalk.model.response.AuthenticationResponse;
import sba301.java.opentalk.service.AuthenticationService;
import sba301.java.opentalk.service.JWTService;
import sba301.java.opentalk.service.MailService;
import sba301.java.opentalk.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JWTService jwtService;
    private final MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<AuthenticationResponse> response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        ApiResponse<AuthenticationResponse> response = authenticationService.login(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String accessToken) {
        ApiResponse<String> response = authenticationService.logout(accessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(@RequestHeader("Authorization") String refreshToken) throws AppException {
        ApiResponse<AuthenticationResponse> response = authenticationService.refresh(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) throws AppException {
        boolean emailExists = userService.checkIfEmailExists(request.getEmail());
        if (!emailExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder().message("Email not found!").build());
        }

        String token = jwtService.generatePasswordResetToken(request.getEmail());

        mailService.sendPasswordResetMail(request.getEmail(), token);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder().message("Password reset link has been sent to your email").build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) throws AppException {
        String email = jwtService.validatePasswordResetToken(request.getToken());
        if (email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder().message("Invalid or expired token!").build());
        }
        userService.updatePassword(email, request.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<String>builder().message("Password successfully reset").build());
    }
}
