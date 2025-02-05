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
        if (redisProperties.getNodes().size() > 1) {
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getNodes());
            clusterConfig.setMaxRedirects(redisProperties.getMaxRedirects());
            return new LettuceConnectionFactory(clusterConfig);
        }
        String[] firstNode = redisProperties.getNodes().get(0).split(":");
        String host = firstNode[0];
        String p = firstNode[1];
        Integer port = Integer.parseInt(p);
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
