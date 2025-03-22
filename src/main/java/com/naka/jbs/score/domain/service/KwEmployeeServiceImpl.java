package com.naka.jbs.score.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naka.jbs.score.domain.model.entity.score.KwEmployee;
import com.naka.jbs.score.domain.repository.score.KwEmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KwEmployeeServiceImpl implements KwEmployeeService {

    private final KwEmployeeRepository kwEmployeeRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<KwEmployee> getAll() {
        return kwEmployeeRepository.findAll();
    }

    @Override
    public void postKwEmployee() {
        getAll().forEach(kwEmployee -> {
            Map<String, Object> map = objectMapper.convertValue(kwEmployee, new TypeReference<Map<String, Object>>() {});
            String key = "kwEmployee" + ":" + kwEmployee.getUid();
            redisTemplate.opsForHash().putAll(key, map);
        });

    }

}
