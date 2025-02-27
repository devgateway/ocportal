package org.devgateway.toolkit.persistence.dao.prequalification;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Octavian Ciubotaru
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {UniquePrequalifiedSupplierItemConstraintValidator.class})
@Documented
public @interface UniquePrequalifiedSupplierItem {

    String message() default
            "{org.devgateway.toolkit.persistence.dao.prequalification.UniquePrequalifiedSupplierItem.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
