package org.devgateway.toolkit.forms.validators;

import java.time.LocalDate;

/**
 * @author Nadejda Mandrescu
 */
public class EarlierThanLocalDateValidator extends AbstractEarlierThanDateValidator<LocalDate> {
    private static final long serialVersionUID = 3649981314668993772L;

    public EarlierThanLocalDateValidator(final LocalDate highDate) {
        super(highDate);
    }

    protected boolean isBefore(final LocalDate highValue, final LocalDate currentValue) {
        return highValue.isBefore(currentValue);
    }
}
