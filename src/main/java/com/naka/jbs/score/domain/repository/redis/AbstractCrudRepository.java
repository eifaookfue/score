package com.naka.jbs.score.domain.repository.redis;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public <S extends T> void save(S entity) {
        Map<String, Object> map = objectMapper.convertValue(entity, new TypeReference<Map<String, Object>>() {});
        String entityName = toLowerCase(entity.getClass().getSimpleName());
        String id = getId(entity);
        String key = entityName + ":" + id;
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.opsForSet().add("__" + entityName, id);
    }

    private String toLowerCase(String text) {
        char c[] = text.toCharArray();
        c[0] += 32;
        return new String(c);
    }

    // TODO: Embeddableの考慮
    private <S extends T> String getId(S entity) {
        return getIdField(getEntityClass()).map(f -> this.<String>getValue(f, entity)).get();
    }

    private Optional<Field> getIdField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    private <V> V getValue(Field field, Object target) {
        field.setAccessible(true);
        try {
            return (V) field.get(target);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findAll() {
        String entityName = toLowerCase(getEntityClass().getSimpleName());
        Set<Object> keys = redisTemplate.opsForSet().members("__" + entityName);
        return keys.stream().map(String.class::cast).map(k -> {
            String key = entityName + ":" + k;
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
            return objectMapper.convertValue(map, getEntityClass());
        }).collect(Collectors.toList());

    }
}
