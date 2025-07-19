package sba301.java.opentalk.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    void saveRefreshToken(long userId, String refreshToken, long duration);
    String getRefreshToken(long userId);
    void deleteRefreshToken(long userId);
    boolean isRefreshTokenExpired(long userId);

    void saveRandomDateCron(String cronExpression);
    String getRandomDateCron();

    void saveDaysUntilOpenTalk(int daysUntilOpenTalk);
    int getDaysUntilOpenTalk();

    void saveSyncDateCron(String cronExpression);
    String getSyncDateCron();

    void revokeToken(String accessToken, long ttl);
    boolean isTokenRevoked(String accessToken);
    void savePollScheduleCron(long pollId, String cronExpression);
}