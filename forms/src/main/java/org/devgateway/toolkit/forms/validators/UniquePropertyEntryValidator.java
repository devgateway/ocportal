package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Optional;

/**
 * Ensure the property value is unique across saved entities vs the entity being validated
 *
 * @param <T> the enclosing entity type
 * @param <P> the property type
 */
public class UniquePropertyEntryValidator<T extends AbstractAuditableEntity, P> implements IValidator<P>, Serializable {

    private String message;
    private SerializableFunction<Specification<T>, Optional<T>> repositorySearcher;
    private IModel<T> formModel;
    private SpecificationConversion<T, P> specificationConversion;


    public UniquePropertyEntryValidator(final String message,
                                        final SerializableFunction<Specification<T>, Optional<T>> repositorySearcher,
                                        final SpecificationConversion<T, P> specificationConversion,
                                        final IModel<T> formModel) {
        this.message = message;
        this.repositorySearcher = repositorySearcher;
        this.formModel = formModel;
        this.specificationConversion = specificationConversion;
    }

    @Override
    public void validate(final IValidatable<P> validatable) {
        Optional<T> other = repositorySearcher.apply(specificationConversion.toSpecification(
                formModel.getObject(),
                validatable.getValue()
        ));
        if (other.isPresent() && !other.get().getId().equals(formModel.getObject().getId())) {
            final ValidationError error = new ValidationError(message);
            validatable.error(error);
        }

    }

    @FunctionalInterface
    public interface SpecificationConversion<Z extends AbstractAuditableEntity, V> extends Serializable {
        Specification<Z> toSpecification(Z object, V value);
    }

}
