package org.devgateway.toolkit.forms.validators;

import org.apache.poi.ss.formula.functions.T;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.time.LocalDate;
import java.util.Date;

/**
 * Validator to test if the date is earlier than provided high date
 *
 * @author idobre
 */
public abstract class AbstractEarlierThanDateValidator<T> implements IValidator<T> {
    private static final long serialVersionUID = 8933283763474434650L;

    private T highDate;

    public AbstractEarlierThanDateValidator(final T highDate) {
        this.highDate = highDate;
    }

    @Override
    public void validate(final IValidatable<T> validatable) {
        if (highDate == null) {
            return;
        }

        if (validatable.getValue() != null && isBefore(highDate, validatable.getValue())) {
            ValidationError error = new ValidationError();
            error.addKey("AbstractEarlierThanDateValidator");
            error.setVariable("highDate", highDate);
            validatable.error(error);
        }
    }

    protected abstract boolean isBefore(T highValue, T currentValue);

}
