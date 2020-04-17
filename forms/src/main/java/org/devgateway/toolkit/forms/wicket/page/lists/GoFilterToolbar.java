package org.devgateway.toolkit.forms.wicket.page.lists;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoFilter;

/**
 * @author mpostelnicu
 */
public class GoFilterToolbar extends FilterToolbar {

    private GoFilter goFilter;

    /**
     * Constructor
     *
     * @param table data table this toolbar will be added to
     * @param form  the filter form
     */
    public <T, S, F> GoFilterToolbar(final DataTable<T, S> table, final GoFilter goFilter,
                                     final FilterForm<F> form) {
        super(table, form);
        this.goFilter = goFilter;
        add(this.goFilter);
    }

    public GoFilter getGoFilter() {
        return goFilter;
    }
}
