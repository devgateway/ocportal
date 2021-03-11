package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.model.IModel;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class EntityListChoiceProvider<T extends GenericPersistable>
        extends SimpleListChoiceProvider<T> {

    public EntityListChoiceProvider(IModel<List<T>> model) {
        super(model);
    }

    @Override
    public String getIdValue(T object) {
        return object.getId().toString();
    }
}
