package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity;
import org.devgateway.toolkit.persistence.service.form.TenderProcessEntityServiceResolver;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Octavian Ciubotaru
 */
public class UniqueTenderProcessEntityConstraintValidator
        implements ConstraintValidator<UniqueTenderProcessEntity, AbstractTenderProcessClientEntity> {

    @Autowired
    private TenderProcessEntityServiceResolver tenderProcessEntityServiceResolver;

    @Override
    public boolean isValid(AbstractTenderProcessClientEntity entity, ConstraintValidatorContext context) {
        if (entity == null) {
            return true;
        }

        return tenderProcessEntityServiceResolver.countByTenderProcess(entity) == 0;
    }
}
