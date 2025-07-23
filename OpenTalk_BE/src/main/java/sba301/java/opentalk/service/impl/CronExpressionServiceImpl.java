package sba301.java.opentalk.service.impl;

import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sba301.java.opentalk.enums.CronKey;
import sba301.java.opentalk.service.CronExpressionService;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CronExpressionServiceImpl implements CronExpressionService {

    private final JedisPool jedisPool;
    private final Scheduler scheduler;
    private static final String HASH_KEY = "cron:expressions";

    @Override
    public void saveCronExpression(CronKey cronKey, String expression) {
        if (!CronExpression.isValidExpression(expression)) {
            throw new IllegalArgumentException("Invalid cron expression: " + expression);
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(HASH_KEY, cronKey.getKey(), expression);
        }
        try {
            TriggerKey tk = TriggerKey.triggerKey(cronKey.getTriggerName());
            Trigger newTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(tk)
                    .forJob(cronKey.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(expression))
                    .build();
            scheduler.rescheduleJob(tk, newTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to reschedule job " + cronKey, e);
        }
    }

    @Override
    public String getCronExpression(CronKey cronKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            String expr = jedis.hget(HASH_KEY, cronKey.getKey());
            return expr != null ? expr : cronKey.getDefaultExpression();
        }
    }

    @Override
    public Map<String, String> getAllCronExpressions() {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> data = jedis.hgetAll(HASH_KEY);
            return Arrays.stream(CronKey.values())
                    .collect(Collectors.toMap(
                            CronKey::getKey,
                            key -> data.getOrDefault(key.getKey(), key.getDefaultExpression())
                    ));
        }
    }
}