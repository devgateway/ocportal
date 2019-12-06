package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * Ensure the property value is unique across saved entities vs the entity being validated
 *
 * @param <T> the enclosing entity type
 * @param <P> the property type
 */
public class UniquePropertyEntryValidator<T extends AbstractAuditableEntity, P> implements IValidator<P>, Serializable {
    private final String message;

    private final SerializableFunction<Specification<T>, List<T>> repositorySearcher;

    private final IModel<T> formModel;

    private final SpecificationConversion<T, P> specificationConversion;

    protected final Logger logger = LoggerFactory.getLogger(UniquePropertyEntryValidator.class);

    public UniquePropertyEntryValidator(final String message,
                                        final SerializableFunction<Specification<T>, List<T>> repositorySearcher,
                                        final SpecificationConversion<T, P> specificationConversion,
                                        final IModel<T> formModel) {
        this.message = message;
        this.repositorySearcher = repositorySearcher;
        this.formModel = formModel;
        this.specificationConversion = specificationConversion;
    }

    @Override
    public void validate(final IValidatable<P> validatable) {
        List<T> other = repositorySearcher.apply(specificationConversion.toSpecification(
                formModel.getObject(),
                validatable.getValue()
        ));

        if (!other.isEmpty() && (other.size() > 1 || !other.get(0).getId().equals(formModel.getObject().getId()))) {
            final ValidationError error = new ValidationError(message);
            validatable.error(error);
        }
    }

    @FunctionalInterface
    public interface SpecificationConversion<Z extends AbstractAuditableEntity, V> extends Serializable {
        Specification<Z> toSpecification(Z object, V value);
    }

}
