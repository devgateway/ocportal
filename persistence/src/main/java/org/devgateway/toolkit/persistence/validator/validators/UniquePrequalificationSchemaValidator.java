package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author mpostelnicu
 */
public class UniquePrequalificationSchemaValidator
        implements ConstraintValidator<UniquePrequalificationSchema, PrequalificationSchema> {

    @Autowired
    private PrequalificationSchemaService prequalificationSchemaService;

    @Override
    public boolean isValid(PrequalificationSchema schema, ConstraintValidatorContext context) {
        if (schema == null) {
            return true;
        }

        return prequalificationSchemaService.countByNameOrPrefix(schema) == 0;
    }
}
