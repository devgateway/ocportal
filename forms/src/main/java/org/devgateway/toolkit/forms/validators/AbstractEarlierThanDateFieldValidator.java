package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.wicket.components.form.AbstractDateFieldBootstrapFormComponent;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author mpostelnicu {@link AbstractDateFieldBootstrapFormComponent} validator for
 *         dates that have a chronology
 */
public abstract class AbstractEarlierThanDateFieldValidator<T> implements IValidator<T> {
    private static final long serialVersionUID = 5915013097774040799L;

    private final AbstractDateFieldBootstrapFormComponent highDate;

    /**
     * Provide a {@link AbstractDateFieldBootstrapFormComponent} that has to be
     * chronologically after the current's
     * {@link AbstractDateFieldBootstrapFormComponent} validator
     */
    public AbstractEarlierThanDateFieldValidator(final AbstractDateFieldBootstrapFormComponent highDate) {
        this.highDate = highDate;
    }

    @Override
    public void validate(final IValidatable<T> validatable) {
        highDate.getField().validate();
        if (!highDate.getField().isValid()) {
            return;
        }

        final T endDate = (T) highDate.getField().getConvertedInput();

        if (endDate != null && validatable.getValue() != null && isBefore(endDate, validatable.getValue())) {
            final ValidationError error = new ValidationError();
            error.addKey("AbstractEarlierThanDateFieldValidator");
            error.setVariable("highDateName", highDate.getLabelModel().getObject());
            validatable.error(error);
        }
    }

    protected abstract boolean isBefore(T highValue, T currentValue);
}
