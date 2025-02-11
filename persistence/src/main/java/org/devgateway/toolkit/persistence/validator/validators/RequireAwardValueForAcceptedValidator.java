package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class RequireAwardValueForAcceptedValidator
        implements ConstraintValidator<RequireAwardValueForAccepted, AwardAcceptanceItem>  {

    @Override
    public boolean isValid(AwardAcceptanceItem value, ConstraintValidatorContext context) {
        if (value == null || !value.isAccepted()) {
            return true;
        }
        return value.getAcceptedAwardValue() != null;
    }
}
