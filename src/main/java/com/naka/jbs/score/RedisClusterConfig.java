package com.naka.jbs.score;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

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
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);

        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
