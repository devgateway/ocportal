package org.devgateway.toolkit.persistence.validator;

import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.fm.ConstrainedField;
import org.devgateway.toolkit.persistence.fm.ConstraintInfo;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.jpa.JPAUtils;
import org.devgateway.toolkit.persistence.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.Type;
import java.util.HashMap;
import java.util.Map;

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
    private EntityManagerFactory entityManagerFactory;

    public Map<ConstrainedField, ConstraintInfo> getConstraints() {
        Collector collector = new Collector();

        entityManagerFactory.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .filter(e -> e.isAnnotationPresent(Form.class))
                .forEach(collector::collect);

        return collector.getFields();
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

            collect(StringUtils.uncapitalizeCamelCase(javaType.getSimpleName()) + "Form.", javaType);
        }

        private void collect(String prefix, Class<?> javaType) {
            EntityType<?> entity = entityManagerFactory.getMetamodel().entity(javaType);

            for (Attribute<?, ?> attr : entity.getAttributes()) {
                String fmName = prefix + attr.getName();

                visit(attr, fmName, javaType);

                if (JPAUtils.isOneToMany(attr)) {
                    if (JPAUtils.isOwningOneToMany(attr)) {
                        collect(prefix + attr.getName() + ".",
                                ((PluralAttribute<?, ?, ?>) attr).getElementType().getJavaType());
                    }
                } else if (JPAUtils.isOneToOne(attr)) {
                    collect(prefix + attr.getName() + ".", attr.getJavaType());
                } else if (!(JPAUtils.isManyToOne(attr) || JPAUtils.isManyToMany(attr) || JPAUtils.isBasic(attr))) {
                    logger.warn(String.format("Skipping %s.%s",
                            attr.getDeclaringType().getJavaType().getSimpleName(), attr.getName()));
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

            boolean required = dgFmService.hasFeature(fmName) && dgFmService.isFeatureMandatory(fmName);

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
}
