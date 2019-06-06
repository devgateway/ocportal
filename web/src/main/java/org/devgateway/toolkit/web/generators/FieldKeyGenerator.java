package org.devgateway.toolkit.web.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author idobre
 * @since 2019-06-06
 * <p>
 * {@link KeyGenerator} that uses specific object properties to create a key.
 * We don't use {@link ObjectMapper} to generate the key for speed considerations.
 */
public class FieldKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        if (params.length < 1) {
            throw new RuntimeException(
                    "Wrong parameters received for generating custom FieldKeyGenerator key!");
        }

        final StringBuilder key = new StringBuilder(method.getName());

        for (final Object param : params) {
            if (param instanceof List<?>) {
                ((List<?>) param).stream()
                        .forEach(element -> key.append(createKey(element)));
            } else {
                key.append(createKey(param));
            }
        }

        return key.toString();
    }

    private String createKey(final Object param) {
        if (param instanceof Field) {
            return ((Field) param).getName();
        } else {
            if (param instanceof Class) {
                return ((Class) param).getName();
            } else {
                if (param instanceof GenericPersistable) {
                    final GenericPersistable persistable = (GenericPersistable) param;
                    if (persistable.getId() != null) {
                        return Long.toString(persistable.getId());
                    } else {
                        return persistable.toString();
                    }
                } else {
                    return param.toString();
                }
            }
        }
    }
}

