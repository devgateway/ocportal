package org.devgateway.ocds.web.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Created by mpostelnicu on 2/17/17.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "language must not be null")
@Pattern(regexp = "^[a-z]{2}_[A-Z]{2}$", message = "Invalid language!")
public @interface LanguageValidation {
}
