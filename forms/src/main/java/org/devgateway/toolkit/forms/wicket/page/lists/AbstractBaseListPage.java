package org.devgateway.toolkit.forms.wicket.page.lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
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
 * @author Octavian Ciubotaru, mpostelnicu
 */
public abstract class AbstractBaseListPage<T extends GenericPersistable & Serializable> extends BasePage {

    private DataTable<T, String> dataTable;

    private AbstractDataProvider<T> dataProvider;

    protected final List<IColumn<T, String>> columns = new ArrayList<>();

    protected Boolean filterGoReset = false;

    private Component bottomAddButton;

    private Component topAddButton;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public AbstractBaseListPage(PageParameters parameters) {
        super(parameters);
    }

    protected DataTable<T, String> createDatatable() {
        return new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, WebConstants.PAGE_SIZE);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider = createDataProvider();
        dataProvider.setFilterState(newFilterState());

        dataTable = createDatatable();

        initializeColumns();

        Form<?> filterForm = createFilterForm();
        filterForm.add(dataTable);
        add(filterForm);

        bottomAddButton = createBottomAddButton();
        add(bottomAddButton);

        topAddButton = createTopAddButton();
        add(topAddButton);
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

    protected final DataTable<T, String> getDataTable() {
        return dataTable;
    }

    protected abstract AbstractDataProvider<T> createDataProvider();

    protected final AbstractDataProvider<T> getDataProvider() {
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

    protected final Component createBottomAddButton() {
       return createAddButton("new");
    }

    /**
     * Method that creates both new buttons, since in all cases these buttons must have exactly the same functionality
     *ccc
     * @param id
     * @return
     */
    protected Component createAddButton(String id) {
        return new WebMarkupContainer(id).setVisibilityAllowed(false);
    }

    protected final Component getBottomAddButton() {
        return bottomAddButton;
    }

    protected final Component createTopAddButton() {
        return createAddButton("newTop");
    }

    protected final Component getTopAddButton() {
        return topAddButton;
    }
}
