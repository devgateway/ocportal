package org.devgateway.toolkit.persistence.validator.validators;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Octavian Ciubotaru
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {RequireAwardValueForAcceptedValidator.class})
@Documented
public @interface RequireAwardValueForAccepted {

    String message() default "{org.devgateway.toolkit.persistence.validator.validators.RequireAwardValueForAccepted}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
