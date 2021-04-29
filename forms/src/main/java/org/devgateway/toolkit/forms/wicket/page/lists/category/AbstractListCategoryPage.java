package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.category.GenericCategoryFilterState;

/**
 * @author idobre
 * @since 2019-03-11
 */
public abstract class AbstractListCategoryPage<T extends Category> extends AbstractListPage<T> {

    public AbstractListCategoryPage(final PageParameters parameters) {
        super(parameters);
        filterGoReset = true;
    }

    @Override
    protected void addColumns() {
        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("label", AbstractListCategoryPage.this)).getString()),
                "label", "label"));
    }

    @Override
    public JpaFilterState<T> newFilterState() {
        return new GenericCategoryFilterState<>();
    }
}
