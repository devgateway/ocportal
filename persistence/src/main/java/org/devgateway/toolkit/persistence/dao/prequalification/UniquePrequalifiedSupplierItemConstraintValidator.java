package org.devgateway.toolkit.persistence.dao.prequalification;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Octavian Ciubotaru
 */
public class UniquePrequalifiedSupplierItemConstraintValidator
        implements ConstraintValidator<UniquePrequalifiedSupplierItem, PrequalifiedSupplier> {

    @Override
    public boolean isValid(PrequalifiedSupplier value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Set<PrequalificationSchemaItem> schemaItems = new HashSet<>();
        boolean unique = true;
        for (PrequalifiedSupplierItem item : value.getItems()) {
            PrequalificationSchemaItem schemaItem = item.getItem();
            if (schemaItem != null && !schemaItems.add(schemaItem)) {
                unique = false;
                break;
            }
        }
        return unique;
    }
}
