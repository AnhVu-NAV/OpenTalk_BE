package sba301.java.opentalk.service;

import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.model.ApiResponse;
import sba301.java.opentalk.model.request.AuthenticationRequest;
import sba301.java.opentalk.model.request.RegisterRequest;
import sba301.java.opentalk.model.response.AuthenticationResponse;

public interface AuthenticationService {
    ApiResponse<AuthenticationResponse> register(RegisterRequest request);

    ApiResponse<AuthenticationResponse> login(AuthenticationRequest authenticationRequest);

    ApiResponse<String> logout(String accessToken);

    ApiResponse<AuthenticationResponse> refresh(String refreshToken) throws AppException;
}
