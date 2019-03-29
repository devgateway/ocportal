package org.devgateway.toolkit.forms.wicket.components;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-03-27
 */
public abstract class FilteredListModel<T> extends LoadableDetachableModel<List<T>> {
    private final IModel<? extends List<? extends T>> listModel;

    public FilteredListModel(final IModel<List<T>> listModel) {
        this.listModel = listModel;
    }

    @Override
    protected final List<T> load() {
        return listModel.getObject().parallelStream()
                .filter(this::accept)
                .collect(Collectors.toList());
    }

    protected abstract boolean accept(T t);
}
