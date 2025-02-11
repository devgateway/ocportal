package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class OnePlanPerDepartmentAndFYValidator
        implements ConstraintValidator<OnePlanPerDepartmentAndFY, ProcurementPlan> {

    @Autowired
    private ProcurementPlanService service;

    @Override
    public boolean isValid(ProcurementPlan plan, ConstraintValidatorContext context) {
        if (plan.getDepartment() == null || plan.getFiscalYear() == null) {
            return true;
        }
        Long exceptId = plan.getId() == null ? -1L : plan.getId();
        return service.countByDepartmentAndFiscalYear(plan.getDepartment(), plan.getFiscalYear(), exceptId) == 0;
    }
}
