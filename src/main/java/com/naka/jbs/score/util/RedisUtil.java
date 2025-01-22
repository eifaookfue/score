package com.naka.jbs.score.util;

import java.util.Objects;

import org.springframework.data.redis.core.RedisHash;

public class RedisUtil {

    public static String getBaseKey(Class<?> clazz) {
        RedisHash annotation = clazz.getAnnotation(RedisHash.class);
        if (Objects.isNull(annotation)) {
            return "";
        }
        return annotation.value();
    }

    public static String createKey(String key, Class<?> clazz) {
        String baseKey = getBaseKey(clazz);
        return baseKey + ":" + key;
    }
}
