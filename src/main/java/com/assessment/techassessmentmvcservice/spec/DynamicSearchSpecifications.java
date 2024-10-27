package com.assessment.techassessmentmvcservice.spec;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public class DynamicSearchSpecifications {
    private static final Logger log = LoggerFactory.getLogger(DynamicSearchSpecifications.class);
    public static <T> Specification<T> hasField(Class<T> entityClass, String field, String value) {
        return (root, query, cb) -> {
            try {
                Field declaredField = entityClass.getDeclaredField(field);
                declaredField.setAccessible(true);
                if (Integer.class.isAssignableFrom(declaredField.getType())) {
                    return cb.equal(root.get(field), Integer.valueOf(value));
                } else if (String.class.isAssignableFrom(declaredField.getType())) {
                    return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
                }
            } catch (NoSuchFieldException e) {
                log.error("Unsupported field: " + field);
                throw new IllegalArgumentException("Unsupported field: " + field);
            }
            return null;
        };
    }
}