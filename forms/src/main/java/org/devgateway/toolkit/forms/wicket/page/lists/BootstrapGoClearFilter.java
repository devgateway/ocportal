package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu
 */
public class BootstrapGoClearFilter extends GoAndClearFilter {

    public BootstrapGoClearFilter(final String id, final FilterForm<?> form) {
        super(id, form);
    }

    public BootstrapGoClearFilter(final String id, final FilterForm<?> form, final IModel<String> goModel,
                                  final IModel<String> clearModel) {
        super(id, form, goModel, clearModel);
    }

    public BootstrapGoClearFilter(final String id, final IModel<String> goModel, final IModel<String> clearModel,
                                  final Object originalState) {
        super(id, goModel, clearModel, originalState);
    }
}
