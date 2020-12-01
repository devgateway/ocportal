package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

import java.math.BigDecimal;

/**
 * @author mpostelnicu
 */
public class NonZeroBigDecimalValidator extends BigDecimalValidator {

    @Override
    public void validate(final IValidatable<BigDecimal> validatable) {
        super.validate(validatable);
        if (BigDecimal.ZERO.equals(validatable.getValue())) {
            final ValidationError error = new ValidationError();
            error.addKey("NonZeroBigDecimalValidator");
            validatable.error(error);
        }
    }
}
