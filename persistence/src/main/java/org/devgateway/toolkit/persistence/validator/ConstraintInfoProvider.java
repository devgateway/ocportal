package org.devgateway.toolkit.persistence.validator;

import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.fm.ConstrainedField;
import org.devgateway.toolkit.persistence.fm.ConstraintInfo;
import org.devgateway.toolkit.persistence.fm.Constraints;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.jpa.JPAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collects all form attributes that are required.
 *
 * @author Octavian Ciubotaru
 */
@Component
public class ConstraintInfoProvider {

    private final Logger logger = LoggerFactory.getLogger(ConstraintInfoProvider.class);

    @Autowired
    private DgFmService dgFmService;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }

    private EntityManagerFactory entityManagerFactory;


    public Constraints getConstraints() {
        Collector collector = new Collector();

        List<Class<?>> roots = entityManagerFactory.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .filter(e -> e.isAnnotationPresent(Form.class))
                .collect(Collectors.toList());

        roots.forEach(collector::collect);

        return new Constraints(roots, collector.getFields());
    }

    /**
     * Walks the form recursively and collects constraints for all fields.
     */
    private class Collector {

        /**
         * Maps fields to constraint info.
         */
        private final Map<ConstrainedField, ConstraintInfo> fields = new HashMap<>();

        void collect(Class<?> javaType) {
            if (logger.isDebugEnabled()) {
                logger.debug("Collecting required attributes for root {}", javaType);
            }

            Form form = javaType.getAnnotation(Form.class);

            collect(form.featureName() + ".", javaType);
        }

        private void collect(String prefix, Class<?> javaType) {
            EntityType<?> entity = entityManagerFactory.getMetamodel().entity(javaType);

            for (Attribute<?, ?> attr : entity.getAttributes()) {
                String fmName = prefix + attr.getName();

                if (isFeatureVisible(fmName)) {
                    visit(attr, fmName, javaType);

                    if (JPAUtils.isOneToMany(attr)) {
                        if (JPAUtils.isOwningOneToMany(attr)) {
                            collect(prefix + attr.getName() + ".",
                                    ((PluralAttribute<?, ?, ?>) attr).getElementType().getJavaType());
                        }
                    } else if (JPAUtils.isOneToOne(attr)) {
                        collect(prefix + attr.getName() + ".", attr.getJavaType());
                    } else if (!(JPAUtils.isManyToOne(attr) || JPAUtils.isManyToMany(attr) || JPAUtils.isBasic(attr))) {
                        throw new RuntimeException(String.format("Not implemented case for %s.%s",
                                attr.getDeclaringType().getJavaType().getSimpleName(), attr.getName()));
                    }
                }
            }
        }

        /**
         * @param attr for the field to which to apply the constraints
         * @param fmName controls the constraints
         * @param javaType java type that has the field (not where the field was declared)
         */
        public void visit(Attribute<?, ?> attr, String fmName, Class<?> javaType) {
            boolean cascadeValidation = JPAUtils.isOwningOneToMany(attr) || JPAUtils.isOneToOne(attr);
            ConstrainedField field = new ConstrainedField(attr);

            boolean required = isFeatureMandatory(fmName);

            ConstraintInfo constraintInfo = fields.computeIfAbsent(field, f -> new ConstraintInfo(cascadeValidation));
            if (required) {
                if (constraintInfo.getOptionalForJavaTypes().contains(javaType)) {
                    throw new IllegalStateException("Field is both optional and required. Please check "
                            + javaType + " and " + constraintInfo.getOptionalForJavaTypes());
                }

                constraintInfo.getRequiredForJavaTypes().add(javaType);
            } else {
                if (constraintInfo.getRequiredForJavaTypes().contains(javaType)) {
                    throw new IllegalStateException("Field is both optional and required. Please check "
                            + javaType + " and " + constraintInfo.getRequiredForJavaTypes());
                }

                constraintInfo.getOptionalForJavaTypes().add(javaType);
            }
        }

        public Map<ConstrainedField, ConstraintInfo> getFields() {
            return fields;
        }
    }

    private boolean isFeatureVisible(String featureName) {
        return dgFmService.hasFeature(featureName) && dgFmService.isFeatureVisible(featureName);
    }

    private boolean isFeatureMandatory(String featureName) {
        return dgFmService.hasFeature(featureName) && dgFmService.isFeatureMandatory(featureName);
    }
}
