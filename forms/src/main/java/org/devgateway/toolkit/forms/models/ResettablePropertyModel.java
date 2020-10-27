package org.devgateway.toolkit.forms.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Use case: subform in a dialog/panel with Cancel button to rollback changes in the dialog.
 *  - define all properties within that dialog/panel wrapper in this class (similar for any sublist)
 *  - on cancel, visit with GenericBootstrapFormComponentResetVisitor to reset on all properties
 *  - on cancel, refresh any display of T (or its parent)
 *
 * @author Nadejda Mandrescu
 */
public class ResettablePropertyModel<T extends Serializable> extends PropertyModel<T> implements ResettableModel<T> {
    private static final long serialVersionUID = 67782077739075533L;

    private final IModel<T> initialValueModel;

    private final Class clazz;

    public ResettablePropertyModel(final Object modelObject, final String expression) {
        super(modelObject, expression);

        T value = this.getObject();
        this.clazz = value == null ? null : value.getClass();
        this.initialValueModel = toModel(value);
    }

    private IModel<T> toModel(final T value) {
        if (isCollection(clazz)) {
            List list = new ArrayList((Collection) value);
            return new ListModel(list);
        }
        return new Model<>(value);
    }

    private boolean isCollection(final Class clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public void reset() {
        T value = this.getObject();
        if (isCollection(clazz)) {
            Collection collection = (Collection) value;
            collection.clear();
            collection.addAll((Collection) initialValueModel.getObject());
        } else {
            value = initialValueModel.getObject();
        }
        this.setObject(value);
    }

}
