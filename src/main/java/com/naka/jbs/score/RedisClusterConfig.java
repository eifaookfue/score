package com.naka.jbs.score;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisClusterConfig {
    @Bean
    LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getNodes());
        clusterConfig.setMaxRedirects(redisProperties.getMaxRedirects());
        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
