package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.categories.Category;

/**
 * @author idobre
 * @since 2019-03-11
 */
public abstract class AbstractListCategoryPage<T extends Category> extends AbstractListPage<T> {

    public AbstractListCategoryPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        // just replace the page title with the name of the class
        // instead of having .properties files only for the page title
        addOrReplace(new Label("pageTitle",
                StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(
                        this.getClass().getSimpleName().replaceAll("List", "").replaceAll("Page", "")), ' ')
                        + " Metadata List"));


        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("label", AbstractListCategoryPage.this)).getString()),
                "label", "label"));

        super.onInitialize();
    }
}
