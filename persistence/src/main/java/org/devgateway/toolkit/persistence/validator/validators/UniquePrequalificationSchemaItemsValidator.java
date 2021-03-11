package org.devgateway.toolkit.persistence.validator.validators;

import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author mpostelnicu
 */
public class UniquePrequalificationSchemaItemsValidator
        implements ConstraintValidator<UniquePrequalificationSchemaItems, PrequalificationSchema> {


    public static boolean wrongDistinctItemCount(Collection<PrequalificationSchemaItem> col,
                                                     SerializableFunction<PrequalificationSchemaItem, Comparable<?>>
                                                             comparingSupplier) {
        long distinctCount = col.stream().map(comparingSupplier).distinct().count();
        return distinctCount != col.size();
    }

    @Override
    public boolean isValid(PrequalificationSchema schema, ConstraintValidatorContext context) {
        return !wrongDistinctItemCount(schema.getItems(), PrequalificationSchemaItem::getCode)
                && !wrongDistinctItemCount(schema.getItems(), PrequalificationSchemaItem::getName);
    }
}
