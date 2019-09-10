package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.util.Date;

/**
 * @author idobre
 * @since 10/09/2019
 * <p>
 * Validator to test if the date is after than provided date.
 */
public class AfterThanDateValidator implements IValidator<Date> {
    private Date date;

    public AfterThanDateValidator(final Date date) {
        this.date = date;
    }

    @Override
    public void validate(final IValidatable<Date> validatable) {
        if (date == null) {
            return;
        }

        if (validatable.getValue() != null && date.after(validatable.getValue())) {
            ValidationError error = new ValidationError(this);
            error.setVariable("lowDate", date);
            validatable.error(error);
        }
    }
}
