package sba301.java.opentalk.service;

import java.util.List;

public interface RedisService {
    String get(String key);

    long getRemainingTtl(String key);

    void saveRefreshToken(long userId, String refreshToken, long duration);

    String getRefreshToken(long userId);

    void deleteRefreshToken(long userId);

    boolean isRefreshTokenExpired(long userId);

    String getRandomDateCron();

    int getDaysUntilOpenTalk();

    String getSyncDateCron();

    void revokeToken(String accessToken, long ttl);

    boolean isTokenRevoked(String accessToken);

    void saveKeyWithTTL(String key, String value, long ttlSeconds);

    List<String> getKeysByPattern(String pattern);

}