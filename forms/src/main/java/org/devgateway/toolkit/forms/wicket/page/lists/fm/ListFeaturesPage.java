package org.devgateway.toolkit.forms.wicket.page.lists.fm;

import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableList;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.forms.wicket.components.form.BootstrapSubmitButton;
import org.devgateway.toolkit.forms.wicket.components.form.RadioGroupBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.table.AjaxFallbackBootstrapDataTable;
import org.devgateway.toolkit.forms.wicket.components.table.StyledLambdaColumn;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.forms.wicket.styles.ListFeaturesStyles;
import org.devgateway.toolkit.persistence.fm.entity.DgFeature;
import org.devgateway.toolkit.persistence.fm.entity.UnchainedDgFeature;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@MountPath
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class ListFeaturesPage extends BasePage {

    public static final ImmutableList<String> BOOL_OPTS = ImmutableList.of(Filter.ALL, Filter.YES, Filter.NO);

    @SpringBean
    private DgFmService dgFmService;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public ListFeaturesPage(PageParameters parameters) {
        super(parameters);

        ExplainModal explainModal = new ExplainModal("explainModal", Model.of());
        add(explainModal);

        Model<Filter> filterModel = Model.of(new Filter());

        AjaxFallbackBootstrapDataTable<DgFeature, String> dataTable = createDataTable(explainModal, filterModel);
        add(dataTable);

        Form<Filter> filterForm = createFilterForm(filterModel, dataTable);
        add(filterForm);
    }

    private AjaxFallbackBootstrapDataTable<DgFeature, String> createDataTable(ExplainModal explainModal,
            Model<Filter> filterModel) {
        List<IColumn<DgFeature, String>> columns = new ArrayList<>();

        columns.add(new Column<>(this, "name", "name", UnchainedDgFeature::getName));

        columns.add(new Column<>(this, "visible", "visible", UnchainedDgFeature::getVisible));

        columns.add(new Column<>(this, "enabled", "enabled", UnchainedDgFeature::getEnabled));

        columns.add(new Column<>(this, "mandatory", "mandatory", UnchainedDgFeature::getMandatory));

        columns.add(new Column<>(this, "visibleDeps", getDepsColumnNameFn(DgFeature::getChainedVisibleDeps)));

        columns.add(new Column<>(this, "enabledDeps", getDepsColumnNameFn(DgFeature::getChainedEnabledDeps)));

        columns.add(new Column<>(this, "mandatoryDeps", getDepsColumnNameFn(DgFeature::getChainedMandatoryDeps)));

        columns.add(new Column<>(this, "mixins", getDepsColumnNameFn(DgFeature::getChainedMixins)));

        columns.add(new AbstractColumn<DgFeature, String>(new StringResourceModel("actions", this)) {
            @Override
            public void populateItem(Item<ICellPopulator<DgFeature>> cellItem, String componentId,
                    IModel<DgFeature> rowModel) {
                Fragment actions = new Fragment(componentId, "actionsCell", ListFeaturesPage.this);

                BootstrapAjaxLink<DgFeature> link = new BootstrapAjaxLink<DgFeature>("explain", rowModel,
                        Buttons.Type.Default,
                        new StringResourceModel("explain", ListFeaturesPage.this)) {

                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        explainModal.setModelObject(rowModel.getObject());
                        explainModal.show(true);
                        target.add(explainModal);
                    }
                };
                link.setSize(Buttons.Size.Small);

                actions.add(link);
                cellItem.add(actions);
            }

            @Override
            public String getCssClass() {
                return "actions";
            }
        });

        AjaxFallbackBootstrapDataTable<DgFeature, String> dataTable;
        dataTable = new AjaxFallbackBootstrapDataTable<>("table", columns,
                new FeaturesDataProvider(dgFmService, filterModel), 10);
        dataTable.add(new CssClassNameAppender("featuresTable"));
        return dataTable;
    }

    private static class Column<T, S> extends StyledLambdaColumn<T, S> {

        public Column(Component component, String name, SerializableFunction<T, ?> function) {
            super(new StringResourceModel(name, component), name, function);
        }

        public Column(Component component, String name, S sortProperty, SerializableFunction<T, ?> function) {
            super(new StringResourceModel(name, component), name, sortProperty, function);
        }

        @Override
        public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
            super.populateItem(item, componentId, rowModel);
            item.get(componentId).add(new TooltipBehavior(() -> getDataModel(rowModel).getObject().toString()));
        }
    }

    private Form<Filter> createFilterForm(Model<Filter> filterModel,
            AjaxFallbackBootstrapDataTable<DgFeature, String> dataTable) {

        Form<Filter> filterForm = new Form<>("filterForm", new CompoundPropertyModel<>(filterModel));

        filterForm.add(new TextFieldBootstrapFormComponent<>("feature"));

        filterForm.add(new RadioGroupBootstrapFormComponent<>("visible", BOOL_OPTS));

        filterForm.add(new RadioGroupBootstrapFormComponent<>("enabled", BOOL_OPTS));

        filterForm.add(new RadioGroupBootstrapFormComponent<>("mandatory", BOOL_OPTS));

        filterForm.add(new Select2ChoiceBootstrapFormComponent<>("visibleDep",
                new GenericChoiceProvider<>(getPossibleDepsOfType(DgFeature::getChainedVisibleDeps))));

        filterForm.add(new Select2ChoiceBootstrapFormComponent<>("enabledDep",
                new GenericChoiceProvider<>(getPossibleDepsOfType(DgFeature::getChainedEnabledDeps))));

        filterForm.add(new Select2ChoiceBootstrapFormComponent<>("mandatoryDep",
                new GenericChoiceProvider<>(getPossibleDepsOfType(DgFeature::getChainedMandatoryDeps))));

        filterForm.add(new Select2ChoiceBootstrapFormComponent<>("mixin",
                new GenericChoiceProvider<>(getPossibleDepsOfType(DgFeature::getChainedMixins))));

        filterForm.add(new BootstrapSubmitButton("submit", new ResourceModel("submit")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                dataTable.setCurrentPage(0);
                target.add(dataTable);
            }
        });
        return filterForm;
    }

    private List<String> getPossibleDepsOfType(Function<DgFeature, Set<DgFeature>> depsFn) {
        return fmService.getFeatures().stream()
                .flatMap(f -> depsFn.apply(f).stream())
                .map(UnchainedDgFeature::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    private SerializableFunction<DgFeature, String> getDepsColumnNameFn(
            SerializableFunction<DgFeature, Set<DgFeature>> depsFn) {
        return f -> depsFn.apply(f)
                .stream()
                .map(UnchainedDgFeature::getName)
                .collect(joining(", "));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(ListFeaturesStyles.INSTANCE));
    }
}
