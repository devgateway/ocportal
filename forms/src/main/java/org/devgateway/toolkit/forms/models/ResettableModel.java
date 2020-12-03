package org.devgateway.toolkit.forms.models;

import org.apache.wicket.model.IModel;

/**
 * Model capable to rollback changes to the initial state
 * @author Nadejda Mandrescu
 */
public interface ResettableModel<T> extends IModel<T> {

    void reset();

}
