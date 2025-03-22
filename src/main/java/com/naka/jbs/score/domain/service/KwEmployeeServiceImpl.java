package com.naka.jbs.score.domain.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            redisTemplate.opsForSet().add("__kwEmployee", kwEmployee.getUid());
        });

    }

    @Override
    public List<KwEmployee> getAllFromRedis() {
        Set<Object> keys = redisTemplate.opsForSet().members("__kwEmployee");
        return keys.stream().map(String.class::cast).map(k -> {
            String key = "kwEmployee" + ":" + k;
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
            return objectMapper.convertValue(map, KwEmployee.class);
        }).collect(Collectors.toList());
    }

}
