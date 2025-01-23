package com.naka.jbs.score;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
@Data
public class RedisProperties {
    private List<String> nodes;
    private int maxRedirects;
}
