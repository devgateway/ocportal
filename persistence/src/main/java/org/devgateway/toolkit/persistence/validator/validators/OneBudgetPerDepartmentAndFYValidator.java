package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.service.form.FiscalYearBudgetService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class OneBudgetPerDepartmentAndFYValidator
        implements ConstraintValidator<OneBudgetPerDepartmentAndFY, FiscalYearBudget> {

    @Autowired
    private FiscalYearBudgetService service;

    @Override
    public boolean isValid(FiscalYearBudget value, ConstraintValidatorContext context) {
        if (value == null || value.getDepartment() == null || value.getFiscalYear() == null) {
            return true;
        }

        Long exceptId = value.getId() == null ? -1L : value.getId();
        return service.countByDepartmentAndFiscalYear(value.getDepartment(), value.getFiscalYear(), exceptId) == 0;
    }
}
