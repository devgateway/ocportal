package org.devgateway.toolkit.persistence.validator.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Checks that there is only one procurement plan per department / fy.
 *
 * @author Octavian Ciubotaru
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {OnePlanPerDepartmentAndFYValidator.class})
@Documented
public @interface OnePlanPerDepartmentAndFY {

    String message() default
            "{org.devgateway.toolkit.persistence.dao.form.OnePlanPerDepartmentAndFYConstraintValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
