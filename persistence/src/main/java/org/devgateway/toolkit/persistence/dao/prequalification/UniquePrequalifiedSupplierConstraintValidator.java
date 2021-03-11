package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class UniquePrequalifiedSupplierConstraintValidator
        implements ConstraintValidator<UniquePrequalifiedSupplier, PrequalifiedSupplier> {

    @Autowired
    private PrequalifiedSupplierService service;

    @Override
    public boolean isValid(PrequalifiedSupplier value, ConstraintValidatorContext context) {
        if (value == null || value.getSupplier() == null || value.getYearRange() == null) {
            return true;
        }
        Long id = value.getId() != null ? value.getId() : -1L;
        return !service.isSupplierPrequalified(value.getSupplier(), value.getYearRange(), id);
    }
}
