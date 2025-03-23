package com.naka.jbs.score.domain.repository.redis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.EmbeddedId;
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
        Map<String, Object> map = convertValue(entity);
        String entityName = toLowerCase(entity.getClass().getSimpleName());
        String id = getId(entity).or(() -> getEmbeddedId(entity)).orElseThrow(() -> new IllegalArgumentException("Entityには@Idか@Embeddableのどちらかを設定したフィールドが必要です"));
        String key = entityName + ":" + id;
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.opsForSet().add("__" + entityName, id);
    }

    @SuppressWarnings("unchecked")
    private <S extends T> Map<String, Object> convertValue(S entity) {
        Map<String, Object> map = objectMapper.convertValue(entity, new TypeReference<Map<String, Object>>() {});
        return map.entrySet().stream().flatMap(e -> {
            if (e.getValue() instanceof Map) {
                return ((Map<String, Object>) e.getValue()).entrySet().stream();
            }
            return Stream.of(e);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x, LinkedHashMap::new));
    }

    private String toLowerCase(String text) {
        char c[] = text.toCharArray();
        c[0] += 32;
        return new String(c);
    }

    private <S extends T> Optional<String> getId(S entity) {
        return getAnnotationField(getEntityClass(), Id.class).map(f -> this.<String>getValue(f, entity));
    }

    private <S extends T> Optional<String> getEmbeddedId(S entity) {
        return getAnnotationField(getEntityClass(), EmbeddedId.class).map(f -> this.getValue(f, entity))
                .map(o -> getAllFields(o).map(Object::toString).collect(Collectors.joining(":")));
    }

    private Optional<Field> getAnnotationField(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotationClass))
                .findFirst();
    }

    private <V> Stream<V> getAllFields(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields()).map(f -> getValue(f, object));
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
