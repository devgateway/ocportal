package org.devgateway.toolkit.forms.validators;

import org.devgateway.toolkit.forms.wicket.components.form.LocalDateFieldBootstrapFormComponent;

import java.time.LocalDate;

/**
 * @author Nadejda Mandrescu
 */
public class EarlierThanLocalDateFieldValidator extends AbstractEarlierThanDateFieldValidator<LocalDate> {
    private static final long serialVersionUID = -519011639871417732L;

    /**
     * Provide a {@link LocalDateFieldBootstrapFormComponent} that has to be
     * chronologically after the current's
     * {@link LocalDateFieldBootstrapFormComponent} validator
     *
     * @param highDate
     */
    public EarlierThanLocalDateFieldValidator(final LocalDateFieldBootstrapFormComponent highDate) {
        super(highDate);
    }

    protected boolean isBefore(final LocalDate highValue, final LocalDate currentValue) {
        return highValue.isBefore(currentValue);
    }
}
