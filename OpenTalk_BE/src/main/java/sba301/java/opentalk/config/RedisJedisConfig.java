package sba301.java.opentalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisJedisConfig {

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), "localhost", 6379);
    }
}