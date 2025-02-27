package org.devgateway.ocds.persistence.mongo.flags;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Octavian Ciubotaru
 */
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFlagName.Validator.class)
public @interface ValidFlagName {

    String message() default "{org.devgateway.ocds.persistence.mongo.flags.ValidFlagName.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    class Validator implements ConstraintValidator<ValidFlagName, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }
            return FlagsConstants.FLAG_VALUES_BY_NAME.containsKey(value);
        }
    }
}

