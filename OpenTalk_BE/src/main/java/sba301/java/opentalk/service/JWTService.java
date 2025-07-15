package sba301.java.opentalk.service;

import sba301.java.opentalk.dto.UserDTO;

import java.util.Date;

public interface JWTService {
    String generateAcessToken(UserDTO dto);
    String generateRefreshToken(UserDTO dto);
    String extractUsername(String token);
    Date extractExpiration(String token);
    long extractUserId(String token);
}