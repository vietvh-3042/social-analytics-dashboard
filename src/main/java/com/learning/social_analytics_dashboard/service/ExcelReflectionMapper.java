package com.learning.social_analytics_dashboard.service;

import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@Component
public class ExcelReflectionMapper {
    public <T> T map(Map<String, String> values, Class<T> type) {
        try {
            T target = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                String raw = values.get(field.getName().toLowerCase(Locale.ROOT));
                if (raw == null) continue;
                field.setAccessible(true);
                if (field.getType().equals(String.class)) field.set(target, raw);
            }
            return target;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("Could not map workbook row", ex);
        }
    }
}
