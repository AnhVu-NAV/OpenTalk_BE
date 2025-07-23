package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sba301.java.opentalk.service.RedisService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CRON_KEY_DAYS_UNTIL_OPENTALK = "cron:daysUntilOpenTalk";
    private static final String CRON_KEY_RANDOM = "cron:random";
    private static final String CRON_KEY_SYNC = "cron:sync";

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public long getRemainingTtl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public void saveRefreshToken(long userId, String refreshToken, long duration) {
        String key = "refresh_token:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, duration / 1000, TimeUnit.SECONDS);
    }

    @Override
    public String getRefreshToken(long userId) {
        String key = "refresh_token:" + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteRefreshToken(long userId) {
        String key = "refresh_token:" + userId;
        redisTemplate.delete(key);
    }

    @Override
    public boolean isRefreshTokenExpired(long userId) {
        String key = "refresh_token:" + userId;
        return redisTemplate.hasKey(key);
    }

    @Override
    public String getRandomDateCron() {
        return (String) redisTemplate.opsForValue().get(CRON_KEY_RANDOM);
    }

    @Override
    public int getDaysUntilOpenTalk() {
        Object value = redisTemplate.opsForValue().get(CRON_KEY_DAYS_UNTIL_OPENTALK);
        if (value != null) {
            return Integer.parseInt(value.toString());
        } else
            return 7;
    }

    @Override
    public String getSyncDateCron() {
        return (String) redisTemplate.opsForValue().get(CRON_KEY_SYNC);
    }

    @Override
    public void revokeToken(String accessToken, long ttl) {
        redisTemplate.opsForValue().set(accessToken, true, ttl, TimeUnit.SECONDS);
    }

    @Override
    public boolean isTokenRevoked(String accessToken) {
        String isRevoked = (String) redisTemplate.opsForValue().get(accessToken);
        return "true".equals(isRevoked);
    }

    @Override
    public void saveKeyWithTTL(String key, String value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public List<String> getKeysByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return new ArrayList<>(keys);
    }
}
