package sba301.java.opentalk.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    public void saveRefreshToken(long userId, String refreshToken, long duration);
    public String getRefreshToken(long userId);
    public void deleteRefreshToken(long userId);
    public boolean isRefreshTokenExpired(long userId);

    public void saveRandomDateCron(String cronExpression);
    public String getRandomDateCron();

    public void saveDaysUntilOpenTalk(int daysUntilOpenTalk);
    public int getDaysUntilOpenTalk();

    public void saveSyncDateCron(String cronExpression);
    public String getSyncDateCron();

    public void revokeToken(String accessToken, long ttl);
    public boolean isTokenRevoked(String accessToken);
}
