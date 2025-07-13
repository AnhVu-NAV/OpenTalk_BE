package sba301.java.opentalk.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import sba301.java.opentalk.dto.UserDTO;
import sba301.java.opentalk.repository.RoleRepository;
import sba301.java.opentalk.service.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.access-token-expiration}")
    private long expirationAccessToken;
    @Value("${jwt.refresh-token-expiration}")
    private long expirationRefreshToken;

    private final RoleRepository roleRepository;

    @Override
    public String generateAcessToken(UserDTO user) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("roles", roleRepository.findById(user.getRole()).get().getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationAccessToken))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("roles", roleRepository.findById(user.getRole()).get().getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            if (expiration.after(new Date())) {
                return claims.getSubject();
            } else return null;
        }
        return null;
    }

    @Override
    public Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            return claims.getExpiration();
        }
        return null;
    }

    @Override
    public long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            Date expiration = claims.getExpiration();
            if (expiration.after(new Date())) {
                return claims.get("id", Integer.class);
            } else return -1;
        }
        return -1;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
