package org.devgateway.toolkit.persistence.validator;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.fm.FMAttribute;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintDef;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
import org.hibernate.validator.cfg.defs.NotEmptyDef;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Configuration;
import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class HibernateValidatorFactoryBean extends LocalValidatorFactoryBean {

    private final Logger logger = LoggerFactory.getLogger(HibernateValidatorFactoryBean.class);

    private final RequiredAttrsBean requiredAttrsBean;

    private static final ClassPool classPool = new ClassPool(true);

    public HibernateValidatorFactoryBean(RequiredAttrsBean requiredAttrsBean) {
        this.requiredAttrsBean = requiredAttrsBean;

        setProviderClass(HibernateValidator.class);
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> genericConfiguration) {
        super.postProcessConfiguration(genericConfiguration);

        addConstraintMapping(genericConfiguration);
    }

    // TODO javadoc + move to a separate class
    private void addConstraintMapping(Configuration<?> genericConfiguration) {
        HibernateValidatorConfiguration configuration = (HibernateValidatorConfiguration) genericConfiguration;

        ConstraintMapping constraintMapping = configuration.createConstraintMapping();

        List<FMAttribute> requiredAttrs = requiredAttrsBean.getRequiredAttributes();

        // TODO build a list of attributes that must be valid, to enable cascade validation

        Set<Class<?>> javaTypes = requiredAttrs.stream()
                .map(FMAttribute::getDeclaringJavaType)
                .collect(Collectors.toSet());

        javaTypes.forEach(javaType -> {
            TypeConstraintMappingContext<?> type = constraintMapping.type(javaType);
            type.defaultGroupSequenceProviderClass(makeGroupSequenceProviderForClass(javaType));

            Map<String, List<FMAttribute>> attrsByField = requiredAttrs.stream()
                    .filter(e -> e.getDeclaringJavaType().equals(javaType))
                    .collect(Collectors.groupingBy(FMAttribute::getFieldName));

            attrsByField.forEach((fieldName, attrs) -> {
                PropertyConstraintMappingContext property = type.property(fieldName, ElementType.FIELD);

                boolean isCollection = attrs.get(0).isCollection();
                boolean isCharSeq = CharSequence.class.isAssignableFrom(attrs.get(0).getJavaType());

                ConstraintDef<?, ?> def;

                if (isCollection || isCharSeq) {
                    def = new NotEmptyDef();
                } else {
                    def = new NotNullDef();
                }

                Class<?>[] groups = attrs.stream()
                        .map(a -> getGroupForClass(a.getJavaType()))
                        .toArray(Class[]::new);
                def.groups(groups);

                if (logger.isDebugEnabled()) {
                    String cn = "@" + StringUtils.removeEnd(def.getClass().getSimpleName(), "Def");
                    cn += "("
                            + Arrays.stream(groups)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", "))
                            + ")";
                    logger.debug("Adding {} on {}.{}", cn, javaType.getSimpleName(), fieldName);
                }

                property.constraint(def);
            });
        });

        configuration.addMapping(constraintMapping);
    }

    private static final Map<Class<?>, Class<?>> groupCache = new HashMap<>();

    private synchronized Class<?> getGroupForClass(Class<?> javaType) {
        return groupCache.computeIfAbsent(javaType, this::makeGroupForClass);
    }

    private Class<?> makeGroupForClass(Class<?> javaType) {
        try {
            String groupClassName = javaType.getName() + "Checks";

            if (logger.isDebugEnabled()) {
                logger.debug("Defining a new group {}", groupClassName);
            }

            CtClass ndClass = classPool.get(NonDraft.class.getName());
            CtClass cc = classPool.makeInterface(groupClassName, ndClass);
            return cc.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException("Failed to create a bean validation group for " + javaType, e);
        }
    }

    private Class makeGroupSequenceProviderForClass(Class<?> javaType) {
        try {
            String gspName = javaType.getName() + "GroupServiceProvider";

            if (logger.isDebugEnabled()) {
                logger.debug("Defining a new group sequence provider {}", gspName);
            }

            CtClass cc = classPool.makeClass(gspName, classPool.get(AbstractFMGroupSequenceProvider.class.getName()));
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
