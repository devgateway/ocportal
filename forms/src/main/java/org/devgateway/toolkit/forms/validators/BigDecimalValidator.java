package org.devgateway.toolkit.forms.validators;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.math.BigDecimal;

/**
 * @author idobre
 * @since 03/09/2019
 *
 * Limit the length of a number to 16 characters.
 */
public class BigDecimalValidator implements IValidator<BigDecimal> {
    private static final int MAX_LENGTH = 16;

    @Override
    public void validate(final IValidatable<BigDecimal> validatable) {
        final BigDecimal value = validatable.getValue();

        if (value != null) {
            final String toString = StringUtils.substringBefore(value.toString(), ".");
            final int length = toString.length();

            if (length > MAX_LENGTH) {
                final ValidationError error = new ValidationError("Number is too big");
                validatable.error(error);
            }
        }

    }
}
