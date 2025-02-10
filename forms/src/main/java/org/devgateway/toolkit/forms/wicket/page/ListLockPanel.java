package org.devgateway.toolkit.forms.wicket.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.LambdaColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.service.TranslateField;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.ResettingFilterForm;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.lists.BootstrapGoClearFilter;
import org.devgateway.toolkit.forms.wicket.page.lists.GoFilterToolbar;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.Lockable;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.form.ClientEntityServiceResolver;
import org.devgateway.toolkit.web.security.SecurityUtil;
import org.hibernate.proxy.HibernateProxyHelper;

/**
 * @author Octavian Ciubotaru
 */
public class ListLockPanel extends Panel {

    @SpringBean
    private ClientEntityServiceResolver clientEntityServiceResolver;

    @SpringBean
    private PersonService personService;

    public ListLockPanel(String id, boolean onlyForPrincipal) {
        super(id);

        setOutputMarkupId(true);

        Person owner = onlyForPrincipal ? SecurityUtil.getCurrentAuthenticatedPerson() : null;
        DataProvider dataProvider = new DataProvider(owner);

        ArrayList<IColumn<Lockable, String>> columns = new ArrayList<>();

        AjaxFallbackBootstrapDataTable<Lockable, String> dataTable =
                new AjaxFallbackBootstrapDataTable<>("table", columns, dataProvider, WebConstants.PAGE_SIZE);

        columns.add(new LambdaColumn<>(new StringResourceModel("formType", this),
                l -> getString(HibernateProxyHelper.getClassWithoutInitializingProxy(l).getSimpleName())));

        columns.add(new LambdaColumn<>(new StringResourceModel("formLabel", this), Lockable::getLabel));

        if (!onlyForPrincipal) {
            columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                    new StringResourceModel("ownerColumn", this), "owner", "owner",
                    Model.ofList(personService.findAll()), dataTable));
        }

        columns.add(new AbstractColumn<Lockable, String>(new StringResourceModel("actionsColumn", this)) {

            @Override
            public void populateItem(final Item<ICellPopulator<Lockable>> cellItem, final String componentId,
                    final IModel<Lockable> model) {
                cellItem.add(new ActionPanel(componentId, model));
            }
        });

        final ResettingFilterForm<FilterState> filterForm =
                new ResettingFilterForm<>("filterForm", dataProvider, dataTable);
        add(filterForm);
        filterForm.add(dataTable);

        final LaddaAjaxButton submit = new LaddaAjaxButton("submit",
                new Model<>("Submit"), Buttons.Type.Default) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                // don't do anything on submit, just refresh the table
                target.add(dataTable);
            }
        };
        filterForm.add(submit);
        filterForm.setDefaultButton(submit);

        if (!onlyForPrincipal) {
            GoAndClearFilter go = new BootstrapGoClearFilter("go", filterForm);
            dataTable.addTopToolbar(new GoFilterToolbar(dataTable, go, filterForm));
        }
    }

    public static class FilterState implements Serializable {

        private Person owner;

        public FilterState(Person owner) {
            this.owner = owner;
        }

        public Person getOwner() {
            return owner;
        }

        public void setOwner(Person owner) {
            this.owner = owner;
        }
    }

    public class DataProvider extends SortableDataProvider<Lockable, String>
            implements IFilterStateLocator<FilterState> {

        private FilterState filterState;
        private List<? extends Lockable> locks;

        public DataProvider(Person owner) {
            filterState = new FilterState(owner);
        }

        @Override
        public Iterator<? extends Lockable> iterator(long first, long count) {
            return getLocks()
                    .stream()
                    .skip(first)
                    .limit(count)
                    .collect(Collectors.toList())
                    .iterator();
        }

        @Override
        public long size() {
            return getLocks().size();
        }

        private List<? extends Lockable> getLocks() {
            if (locks == null) {
                if (filterState.getOwner() != null) {
                    locks = clientEntityServiceResolver.getAllLocked(filterState.getOwner());
                } else {
                    locks = clientEntityServiceResolver.getAllLocked();
                }
            }
            return locks;
        }

        @Override
        public void detach() {
            locks = null;
        }

        @Override
        public IModel<Lockable> model(Lockable object) {
            return Model.of(object);
        }

        @Override
        public FilterState getFilterState() {
            return filterState;
        }

        @Override
        public void setFilterState(FilterState state) {
            filterState = state;
        }
    }

    public class ActionPanel extends Panel {

        public ActionPanel(final String id, final IModel<Lockable> model) {
            super(id, model);

            Lockable lockable = model.getObject();

            boolean ownerIsPrincipal = lockable.getOwner().equals(SecurityUtil.getCurrentAuthenticatedPerson());

            final PageParameters pageParameters = new PageParameters();
            pageParameters.set(WebConstants.PARAM_ID, lockable.getId());

            Class<?> entityClass = HibernateProxyHelper.getClassWithoutInitializingProxy(lockable);
            String entityName = entityClass.getSimpleName();
            Class<? extends GenericWebPage<?>> pageClass = TranslateField.MAP_BEAN_WICKET_PAGE.get(entityName);

            final BootstrapBookmarkablePageLink<?> editPageLink =
                    new BootstrapBookmarkablePageLink<>("edit", pageClass, pageParameters, Buttons.Type.Primary);
            editPageLink.setIconType(FontAwesomeIconType.edit)
                    .setSize(Buttons.Size.Small)
                    .setLabel(new ResourceModel("edit"));

            editPageLink.setVisibilityAllowed(ownerIsPrincipal);
            add(editPageLink);

            BootstrapAjaxLink<Void> unlockLink = new BootstrapAjaxLink<Void>("unlock", Buttons.Type.Warning) {

                @Override
                public void onClick(AjaxRequestTarget target) {
                    clientEntityServiceResolver.unlock(lockable);
                    target.add(ListLockPanel.this);
                }
            };
            unlockLink.setIconType(FontAwesomeIconType.unlock);
            unlockLink.setSize(Buttons.Size.Small);
            unlockLink.setLabel(new ResourceModel("unlock"));
            unlockLink.setVisibilityAllowed(!ownerIsPrincipal);
            add(unlockLink);
        }
    }
}
