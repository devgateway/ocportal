package org.devgateway.toolkit.persistence.validator.validators;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Checks if the number of files attached to a form does not exceed the allowed limit.
 *
 * @author Octavian Ciubotaru
 */
@Size(max = 10)
@ReportAsSingleViolation
@Target({ METHOD })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
@Documented
public @interface MaxAttachedFiles {

    String message() default "{org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
