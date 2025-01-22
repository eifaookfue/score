package com.naka.jbs.score.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;

@RedisHash("user")
@Data
@AllArgsConstructor
public class User {
    @Id
    private String userId;
    private String email;
}
