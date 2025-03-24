package com.naka.jbs.score.domain.repository.redis;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.persistence.EmbeddedId;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.naka.jbs.score.domain.model.entity.score.DlUser;

public class MapToBeanConverter {

    public <T> T convert2(Class<T> clazz, Map<String, Object> source) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            BeanWrapper wrapper = new BeanWrapperImpl(instance);
            Stream.of(wrapper.getPropertyDescriptors()).forEach(pd -> {
                String propName = pd.getName();
                try {
                    Field field = clazz.getDeclaredField(propName);
                    if (field.isAnnotationPresent(EmbeddedId.class)) {
                        Object o = convert2(pd.getPropertyType(), source);
                        wrapper.setPropertyValue(propName, o);
                    } else {
                        wrapper.setPropertyValue(propName, source.get(propName));
                    }
                } catch (NoSuchFieldException | SecurityException e) {
                    return;
                }

            });
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mapのキーを元に、ネストされたBeanにもプロパティをマッピングしてセットする
     */
    public static <T> T convert(Class<T> clazz, Map<String, Object> source) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            BeanWrapper wrapper = new BeanWrapperImpl(instance);

            Map<String, Object> nestedProperties = new HashMap<>();

            for (Map.Entry<String, Object> entry : source.entrySet()) {
                String prop = entry.getKey();
                Object value = entry.getValue();

                if (wrapper.isWritableProperty(prop)) {
                    wrapper.setPropertyValue(prop, value);
                } else {
                    nestedProperties.put(prop, value);
                }
            }

            // ネストされたクラスへの対応（1階層のみ）
            for (PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
                Class<?> propType = pd.getPropertyType();
                String name = pd.getName();

                if (!nestedProperties.isEmpty() && wrapper.isWritableProperty(name)) {
                    Object nestedInstance = propType.getDeclaredConstructor().newInstance();
                    BeanWrapper nestedWrapper = new BeanWrapperImpl(nestedInstance);

                    for (Map.Entry<String, Object> entry : nestedProperties.entrySet()) {
                        if (nestedWrapper.isWritableProperty(entry.getKey())) {
                            nestedWrapper.setPropertyValue(entry.getKey(), entry.getValue());
                        }
                    }

                    wrapper.setPropertyValue(name, nestedInstance);
                }
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Bean conversion failed", e);
        }
    }

    public static void main(String args[]) {
        Map<String, Object> map = Map.of(
                "dlName", "IB",
                "uid", "nakamura",
                "updatedTime", LocalTime.of(19, 0));

//        DlUser user = MapToBeanConverter.convert(DlUser.class, map);
        DlUser user = (new MapToBeanConverter()).convert2(DlUser.class, map);
        System.out.println(user);
    }
}
