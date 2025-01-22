package com.naka.jbs.score.domain.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naka.jbs.score.domain.model.Department;
import com.naka.jbs.score.domain.model.UserDepartment;
import com.naka.jbs.score.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDepartmentRepositoryImpl implements UserDepartmentRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<Department> getDepartments(String userId) {
        String departmentsKey = UserDepartment.createDepartmentsKey(userId);
        List<String> departmentIds = redisTemplate.opsForList().range(departmentsKey, 0, -1);
        List<Object> objects = redisTemplate.executePipelined(new CustomSessionCallback(departmentIds));

        // Objectの実体はLinkedHashMapのため手動で変換する必要がある
        List<Department> departments = objects.stream().map(o -> objectMapper.convertValue(o, Department.class)).collect(Collectors.toList());
        return departments;
    }

    @RequiredArgsConstructor
    private static class CustomSessionCallback implements SessionCallback<String> {
        private final List<String> departmentIds;

        @SuppressWarnings("unchecked")
        @Override
        public <K, V> String execute(RedisOperations<K, V> operations) throws DataAccessException {
            departmentIds.forEach(id -> operations.opsForHash().entries((K) (RedisUtil.createKey(id, Department.class))));
            return null;
        }
    }
}
