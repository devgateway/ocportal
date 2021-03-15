package org.devgateway.toolkit.forms.wicket.providers;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;

import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractDataProvider<T extends Serializable>
        extends SortableDataProvider<T, String>
        implements IFilterStateLocator<JpaFilterState<T>> {
}
