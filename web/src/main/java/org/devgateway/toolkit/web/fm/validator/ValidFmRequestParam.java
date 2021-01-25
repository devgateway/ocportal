package org.devgateway.toolkit.web.fm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {FmRequestParamValidator.class})
@Documented
public @interface ValidFmRequestParam {

    String message() default "Use either fmNames or 3 character or longer fmPrefixes!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}