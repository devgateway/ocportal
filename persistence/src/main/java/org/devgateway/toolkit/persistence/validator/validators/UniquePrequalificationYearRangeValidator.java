package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author mpostelnicu
 */
public class UniquePrequalificationYearRangeValidator
        implements ConstraintValidator<UniquePrequalificationYearRange, PrequalificationYearRange> {

    @Autowired
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @Override
    public boolean isValid(PrequalificationYearRange entity, ConstraintValidatorContext context) {
        if (entity == null) {
            return true;
        }

        return prequalificationYearRangeService.countByName(entity) == 0;
    }
}
