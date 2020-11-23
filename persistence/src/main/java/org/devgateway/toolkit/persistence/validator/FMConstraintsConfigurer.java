package org.devgateway.toolkit.persistence.validator;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.fm.ConstrainedField;
import org.devgateway.toolkit.persistence.fm.ConstraintInfo;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.cfg.defs.NotEmptyDef;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Programmatically add constraints to {@link org.hibernate.validator.HibernateValidator}.
 *
 * Takes all constraints provided by {@link ConstraintInfoProvider} and adds them programmatically to the validator.
 *
 * @author Octavian Ciubotaru
 */
@Component
public class FMConstraintsConfigurer {

    private final Logger logger = LoggerFactory.getLogger(HibernateValidatorFactoryBean.class);

    @Autowired
    private ConstraintInfoProvider constraintInfoProvider;

    /**
     * Private class pool for the generated classes.
     */
    private static final ClassPool CLASS_POOL = new ClassPool(true);

    /**
     * Caching generated classes to avoid generating duplicates.
     */
    private static final Map<Class<?>, Class<?>> GROUP_CACHE = new HashMap<>();

    /**
     * Caching generated classes to avoid generating duplicates.
     */
    private static final Map<Class<?>, Class<?>> GROUP_SEQUENCE_PROVIDER_CACHE = new HashMap<>();

    /**
     * Configure programmatically the constraints.
     */
    public void configure(HibernateValidatorConfiguration configuration) {
        ConstraintMapping constraintMapping = configuration.createConstraintMapping();

        Map<ConstrainedField, ConstraintInfo> constraints = constraintInfoProvider.getConstraints();

        Map<Class<?>, Set<ConstrainedField>> fieldsByDeclaringType = constraints.keySet().stream()
                .collect(groupingBy(ConstrainedField::getDeclaringJavaType, toSet()));

        for (Map.Entry<Class<?>, Set<ConstrainedField>> entry : fieldsByDeclaringType.entrySet()) {
            addConstraintsForClass(constraintMapping, entry.getKey(), entry.getValue(), constraints);
        }

        configuration.addMapping(constraintMapping);
    }

    /**
     * Add constraints for the class.
     */
    private void addConstraintsForClass(ConstraintMapping constraintMapping,
            Class<?> javaType, Set<ConstrainedField> fields,
            Map<ConstrainedField, ConstraintInfo> constraints) {

        TypeConstraintMappingContext<?> type = constraintMapping.type(javaType);
        type.defaultGroupSequenceProviderClass(getGroupSequenceProviderForClass(javaType));

        for (ConstrainedField field : fields) {

            PropertyConstraintMappingContext property = type.property(field.getFieldName(), ElementType.FIELD);

            ConstraintInfo constraintInfo = constraints.get(field);

            if (constraintInfo.isCascadeValidation()) {
                addCascadeForField(field, property);
            }

            if (!constraintInfo.getRequiredForJavaTypes().isEmpty()) {
                addConstraintsForField(javaType, field, property, constraintInfo);
            }
        }
    }

    /**
     * Adds cascade validation to a field.
     */
    private void addCascadeForField(ConstrainedField field, PropertyConstraintMappingContext property) {
        if (logger.isDebugEnabled()) {
            logger.debug("Cascading validation for " + field);
        }

        property.valid();
    }

    /**
     * Adds constraints to a field.
     */
    private void addConstraintsForField(Class<?> javaType, ConstrainedField field,
            PropertyConstraintMappingContext property, ConstraintInfo constraintInfo) {
        boolean isCollection = field.isCollection();
        boolean isCharSeq = CharSequence.class.isAssignableFrom(field.getFieldType());

        ConstraintDef<?, ?> def;

        if (isCollection || isCharSeq) {
            def = new NotEmptyDef();
        } else {
            def = new NotNullDef();
        }

        Class<?>[] groups = constraintInfo.getRequiredForJavaTypes().stream()
                .map(this::getGroupForClass)
                .toArray(Class[]::new);
        def.groups(groups);

        if (logger.isDebugEnabled()) {
            String cn = "@" + StringUtils.removeEnd(def.getClass().getSimpleName(), "Def");
            cn += "("
                    + Arrays.stream(groups)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "))
                    + ")";
            logger.debug("Adding {} on {}.{}", cn, javaType.getSimpleName(), field.getFieldName());
        }

        property.constraint(def);
    }

    /**
     * Generates a bean validation group for a specific class. Group is an interface that extends from {@link NonDraft}
     * and is generated in the same package as the requested class. Name will be appended Checks.
     *
     * Example: for Tender will generate TenderChecks group.
     */
    private synchronized Class<?> getGroupForClass(Class<?> javaType) {
        return GROUP_CACHE.computeIfAbsent(javaType, this::makeGroupForClass);
    }

    /**
     * See {@link #getGroupForClass(Class)}.
     */
    private Class<?> makeGroupForClass(Class<?> javaType) {
        try {
            String groupClassName = javaType.getName() + "Checks";

            if (logger.isDebugEnabled()) {
                logger.debug("Defining a new group {}", groupClassName);
            }

            CtClass ndClass = CLASS_POOL.get(NonDraft.class.getName());
            CtClass cc = CLASS_POOL.makeInterface(groupClassName, ndClass);
            return cc.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException("Failed to create a bean validation group for " + javaType, e);
        }
    }

    private synchronized Class getGroupSequenceProviderForClass(Class<?> javaType) {
        return GROUP_SEQUENCE_PROVIDER_CACHE.computeIfAbsent(javaType, this::makeGroupSequenceProviderForClass);
    }

    /**
     * Generates a class that can be used in @GroupSequenceProvider for a specific entity. The class extends from
     * {@link AbstractFMGroupSequenceProvider} and the generated constructor will invoke super with the appropriate
     * parameters.
     *
     * <p>For Tender will generate the following class:</p>
     * <pre>
     *     public class TenderGroupServiceProvider extends AbstractFMGroupSequenceProvider {
     *         public TenderGroupServiceProvider() {
     *             super(TenderChecks.class, Tender.class);
     *         }
     *     }
     * </pre>
     */
    private Class makeGroupSequenceProviderForClass(Class<?> javaType) {
        try {
            String gspName = javaType.getName() + "GroupServiceProvider";

            if (logger.isDebugEnabled()) {
                logger.debug("Defining a new group sequence provider {}", gspName);
            }

            CtClass cc = CLASS_POOL.makeClass(gspName, CLASS_POOL.get(AbstractFMGroupSequenceProvider.class.getName()));
            CtConstructor c = new CtConstructor(new CtClass[0], cc);
            c.setBody(String.format("super(%s.class, %s.class);",
                    getGroupForClass(javaType).getName(), javaType.getName()));
            cc.addConstructor(c);
            return cc.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException("Failed to create a group service provider for " + javaType, e);
        }
    }
}
