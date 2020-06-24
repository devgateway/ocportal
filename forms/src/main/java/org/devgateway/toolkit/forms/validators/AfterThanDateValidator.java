package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
    private IModel<Date> dateModel;

    public AfterThanDateValidator(final Date date) {
        this.dateModel = Model.of(date);
    }

    public AfterThanDateValidator(IModel<Date> dateModel) {
        this.dateModel = dateModel;
    }

    @Override
    public void validate(final IValidatable<Date> validatable) {
        if (dateModel == null || dateModel.getObject() == null) {
            return;
        }

        if (validatable.getValue() != null && dateModel.getObject().after(validatable.getValue())) {
            ValidationError error = new ValidationError(this);
            error.setVariable("lowDate", dateModel.getObject());
            validatable.error(error);
        }
    }
}
