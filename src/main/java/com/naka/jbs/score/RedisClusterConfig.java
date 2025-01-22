package com.naka.jbs.score;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisClusterConfig {
    @Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(clusterNodes);
        clusterConfig.setMaxRedirects(maxRedirects);
        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
