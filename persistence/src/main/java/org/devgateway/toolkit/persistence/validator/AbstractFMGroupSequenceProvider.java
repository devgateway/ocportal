package org.devgateway.toolkit.persistence.validator;

import com.google.common.collect.ImmutableList;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.List;

/**
 * Conditionally maps Default validation group to the group specified when constructing the provider.
 *
 * If the object being validated is part of a form which is in draft state, then this provider will not change the
 * validation group. This will mean that only constraints for Default group will be used to validate the object.
 *
 * Otherwise, if the form is submitted or does not have such status, then the provider will map Default group to the
 * provided group. Meaning that only constraints for the provided group will be used to validate the object.
 *
 * @author Octavian Ciubotaru
 */
public abstract class AbstractFMGroupSequenceProvider
        implements DefaultGroupSequenceProvider<Object> {

    /**
     * The validation group to map to.
     */
    private final Class<?> group;

    /**
     * Class on which @GroupSequenceProvider is applied.
     */
    private final Class<?> javaType;

    public AbstractFMGroupSequenceProvider(Class<?> group, Class<?> javaType) {
        this.group = group;
        this.javaType = javaType;
    }

    @Override
    public List<Class<?>> getValidationGroups(Object object) {
        if (shouldMapTheGroupFor(object)) {
            return ImmutableList.of(group, javaType);
        } else {
            return ImmutableList.of(javaType);
        }
    }

    // TODO add test for parent null? then return true!
    private boolean shouldMapTheGroupFor(Object object) {
        Object it = object;
        while (it instanceof AbstractChildAuditableEntity) {
            it = ((AbstractChildAuditableEntity<?>) it).getParent();
        }
        if (it instanceof AbstractStatusAuditableEntity) {
            String status = ((AbstractStatusAuditableEntity) it).getStatus();
            return !status.equals(DBConstants.Status.DRAFT)
                    && !status.equals(DBConstants.Status.TERMINATED);
        } else {
            return true;
        }
    }
}
