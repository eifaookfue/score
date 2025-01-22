package com.naka.jbs.score.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;

@RedisHash("department")
@Data
@AllArgsConstructor
public class Department {
    @Id
    private String departmentId;
    private String departmentName;
    private String departmentType;
}
