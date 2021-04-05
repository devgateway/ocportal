package org.devgateway.toolkit.persistence.validator.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Checks that there is only one budget per department / fy.
 *
 * @author Octavian Ciubotaru
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {OneBudgetPerDepartmentAndFYValidator.class})
@Documented
public @interface OneBudgetPerDepartmentAndFY {

    String message() default
            "{org.devgateway.toolkit.persistence.dao.form.OneBudgetPerDepartmentAndFYValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
