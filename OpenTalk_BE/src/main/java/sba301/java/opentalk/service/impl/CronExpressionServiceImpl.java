package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.service.CronExpressionService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CronExpressionServiceImpl implements CronExpressionService {

    private final JedisPool jedisPool;

    private static final String HASH_KEY = "cron:expressions";

    @Override
    public void saveCronExpression(CronKey cronKey, String expression) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(HASH_KEY, cronKey.getKey(), expression);
        }
    }

    @Override
    public String getCronExpression(CronKey cronKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(HASH_KEY, cronKey.getKey());
        }
    }

    @Override
    public Map<String, String> getAllCronExpressions() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(HASH_KEY);
        }
    }
}