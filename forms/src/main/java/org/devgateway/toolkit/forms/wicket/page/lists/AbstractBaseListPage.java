package org.devgateway.toolkit.forms.wicket.page.lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilteredColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.providers.AbstractDataProvider;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractBaseListPage<T extends GenericPersistable & Serializable> extends BasePage {

    private AjaxFallbackBootstrapDataTable<T, String> dataTable;

    private AbstractDataProvider<T> dataProvider;

    protected final List<IColumn<T, String>> columns = new ArrayList<>();

    protected Boolean filterGoReset = false;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public AbstractBaseListPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider = createDataProvider();
        dataProvider.setFilterState(newFilterState());

        dataTable = new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, WebConstants.PAGE_SIZE);

        initializeColumns();

        Form<?> filterForm = createFilterForm();
        filterForm.add(dataTable);
        add(filterForm);

        add(createAddButton("new"));
        add(createTopAddButton("newTop"));
    }

    private Form<?> createFilterForm() {
        final ResettingFilterForm<JpaFilterState<T>> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);

        // create custom submit button in order to prevent form submission
        final LaddaAjaxButton submit = new LaddaAjaxButton("submit",
                new StringResourceModel("submit"), Buttons.Type.Default) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                // don't do anything on submit, just refresh the table
                target.add(dataTable);
            }
        };

        filterForm.add(submit);
        filterForm.setDefaultButton(submit);

        if (hasFilteredColumns()) {
            GoAndClearFilter go = new BootstrapGoClearFilter("go", filterForm);
            GoFilterToolbar filterToolbar = new GoFilterToolbar(dataTable, go, filterForm) {
                @Override
                protected void onInitialize() {
                    super.onInitialize();
                    attachWithParentFm("filterToolbar");
                }
            };
            filterToolbar.setVisibilityAllowed(filterGoReset);
            dataTable.addTopToolbar(filterToolbar);
        }

        return filterForm;
    }

    protected AjaxFallbackBootstrapDataTable<T, String> getDataTable() {
        return dataTable;
    }

    protected abstract AbstractDataProvider<T> createDataProvider();

    public AbstractDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public JpaFilterState<T> newFilterState() {
        return new JpaFilterState<>();
    }

    private boolean hasFilteredColumns() {
        for (final IColumn<?, ?> column : columns) {
            if (column instanceof IFilteredColumn) {
                return true;
            }
        }
        return false;
    }

    private void initializeColumns() {
        columns.add(new AbstractColumn<T, String>(new Model<>("#")) {
            @Override
            public void populateItem(final Item<ICellPopulator<T>> cellItem,
                    final String componentId,
                    final IModel<T> model) {
                final OddEvenItem<?> oddEvenItem = (OddEvenItem<?>) cellItem.getParent().getParent();
                final long index = WebConstants.PAGE_SIZE * dataTable.getCurrentPage() + oddEvenItem.getIndex() + 1;
                cellItem.add(new Label(componentId, index));
            }
        });

        addColumns();

        addActionColumn();
    }

    protected abstract void addColumns();

    protected void addActionColumn() {
    }

    protected Component createAddButton(String id) {
        return new WebMarkupContainer(id).setVisibilityAllowed(false);
    }

    protected Component createTopAddButton(String id) {
        return new WebMarkupContainer(id).setVisibilityAllowed(false);
    }
}
