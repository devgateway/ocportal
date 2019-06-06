package org.devgateway.toolkit.forms.service;

import org.devgateway.toolkit.persistence.excel.service.TranslateService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * @author idobre
 * @since 2019-06-06
 */
@Service
@CacheConfig(keyGenerator = "fieldKeyGenerator", cacheNames = "servicesCache")
@Cacheable
public class TranslateServiceImpl implements TranslateService {
    @Override
    public String getTranslation(final Class clazz, final Field field) {
        final TranslateField translateField = new TranslateField(clazz);

        return translateField.getFieldLabel(field);
    }
}

