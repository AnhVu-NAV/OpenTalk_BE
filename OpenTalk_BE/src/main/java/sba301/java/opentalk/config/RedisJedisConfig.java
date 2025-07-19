package sba301.java.opentalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

@Configuration
public class RedisJedisConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(5);
        poolConfig.setJmxEnabled(false);
        return new JedisPool(poolConfig, "localhost", 6379, Protocol.DEFAULT_TIMEOUT);
    }

}