package org.devgateway.toolkit.persistence.validator;

import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.fm.FMAttribute;
import org.devgateway.toolkit.persistence.jpa.JPAAttribute;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.jpa.JPAUtils;
import org.devgateway.toolkit.persistence.util.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Collects all form attributes that are required.
 *
 * @author Octavian Ciubotaru
 */
@Component
public class RequiredAttrsBean {

    private final Logger logger = LoggerFactory.getLogger(RequiredAttrsBean.class);

    @Autowired
    private DgFmService dgFmService;

    private final Reflections reflections;

    public RequiredAttrsBean() {
        // TODO add package of UserDashboard
        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.devgateway.toolkit.persistence.dao"))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner()));
    }

    public List<FMAttribute> getRequiredAttributes() {
        return collect().entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<FMAttribute, Boolean> collect() {
        Collector collector = new Collector();

        reflections.getTypesAnnotatedWith(Form.class)
                .forEach(collector::collect);

        return collector.fields;
    }

    /**
     * Walks the form recursively and collects all required fields.
     */
    private class Collector {

        /**
         * Maps an attribute to either True if it is required or to False if it is optional.
         */
        Map<FMAttribute, Boolean> fields = new HashMap<>();

        public void collect(Class<?> javaType) {
            if (logger.isDebugEnabled()) {
                logger.debug("Collecting required attributes for root {}", javaType);
            }

            collect(StringUtils.uncapitalizeCamelCase(javaType.getSimpleName()) + "Form.", javaType);
        }

        public void collect(String prefix, Class<?> javaType) {
            Set<Field> refFields = ReflectionUtils.getAllFields(javaType, JPAUtils::isJPAPersistedField);

            for (Field field : refFields) {
                JPAAttribute attr = new JPAAttribute(field);

                String fmName = prefix + attr.getName();

                visit(new FMAttribute(javaType, attr, fmName));

                if (attr.isOneToMany()) {
                    if (attr.isOwningOneToMany()) {
                        collect(prefix + field.getName() + ".", attr.getElementType());
                    } else {
                        // ok, field not owned = no recursion
                    }
                } else if (attr.isOneToOne()) {
                    collect(prefix + field.getName() + ".", attr.getJavaType());
                } else if (attr.isManyToOne() || attr.isManyToMany() || attr.isBasic()) {
                    // ok, field not owned = no recursion
                } else {
                    logger.warn(String.format("Skipping %s.%s",
                            attr.getDeclaringType().getSimpleName(), attr.getName()));
                }
            }
        }

        public void visit(FMAttribute attr) {
            String fmName = attr.getFMName();

            boolean required = dgFmService.hasFeature(fmName) && dgFmService.isFeatureMandatory(fmName);

            Boolean prev = fields.putIfAbsent(attr, required);
            if (prev != null && prev != required) {
                String otherFMName = fields.keySet().stream()
                        .filter(a -> a.equals(attr))
                        .findAny()
                        .map(FMAttribute::getFMName)
                        .orElse(null);

                throw new IllegalStateException(String.format(
                        "Same attribute is both optional and required. Conflicting FM entries: %s, %s.",
                        otherFMName, fmName));
            }
        }
    }

}
