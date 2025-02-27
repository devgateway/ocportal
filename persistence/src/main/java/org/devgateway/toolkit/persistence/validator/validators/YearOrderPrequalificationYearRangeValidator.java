package org.devgateway.toolkit.persistence.validator.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;

/**
 * @author mpostelnicu
 */
public class YearOrderPrequalificationYearRangeValidator
        implements ConstraintValidator<YearOrderPrequalificationYearRange, PrequalificationYearRange> {

    @Override
    public boolean isValid(PrequalificationYearRange entity, ConstraintValidatorContext context) {
        if (entity == null) {
            return true;
        }

        return entity.getStartYear() <= entity.getEndYear();
    }
}
