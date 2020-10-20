package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
import org.devgateway.toolkit.persistence.service.filterstate.form.AbstractTenderProcessMakueniFilterState;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author idobre
 * @since 2019-05-23
 */
public abstract class ListAbstractTenderProcessMakueniEntity<T extends AbstractTenderProcessMakueniEntity>
        extends ListAbstractMakueniEntityPage<T> {

    protected final List<Supplier> awardees;

    @SpringBean
    private SupplierService supplierService;

    public ListAbstractTenderProcessMakueniEntity(final PageParameters parameters) {
        super(parameters);

        this.awardees = supplierService.findAll();
    }

    @Override
    protected void onInitialize() {
        addFmColumn("department", new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "tenderProcess.project.procurementPlan.department",
                "tenderProcess.project.procurementPlan.department",
                new ListModel(departments), dataTable));

        addFmColumn("fiscalYear", new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Fiscal Year"),
                "tenderProcess.project.procurementPlan.fiscalYear",
                "tenderProcess.project.procurementPlan.fiscalYear",
                new ListModel(fiscalYears), dataTable));

        addFmColumn("lastModifiedDate", new PropertyColumn<T, String>(new Model<>("Last Updated Date"),
                "lastModifiedDate", "lastModifiedDate") {
            @Override
            public void populateItem(final Item<ICellPopulator<T>> item,
                                     final String componentId,
                                     final IModel<T> rowModel) {
                final Optional<ZonedDateTime> lastModifiedDate = rowModel.getObject().getLastModifiedDate();

                if (lastModifiedDate.isPresent()) {
                    item.add(new Label(componentId, lastModifiedDate.get().format(DBConstants.DATE_FORMATTER)));
                } else {
                    item.add(new Label(componentId, ""));
                }

            }
        });

        super.onInitialize();
    }

    protected void addAwardeeColumn() {
        addFmColumn("awardee", new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>(
                        (new StringResourceModel("awardee", ListAbstractTenderProcessMakueniEntity.this)).getString()),
                "items.awardee",
                "awardee",
                new ListModel(awardees), dataTable, false
        ));
    }

    protected void addTenderTitleColumn() {

        addFmColumn("tenderTitle", new TextFilteredBootstrapPropertyColumn<T,
                AbstractTenderProcessMakueniFilterState<T>, String>(
                new Model<>(
                        (new StringResourceModel("title", ListAbstractTenderProcessMakueniEntity.this)).getString()),
                null, "tenderProcess.tender.iterator.next.tenderTitle"
        ) {
            @Override
            protected IModel<AbstractTenderProcessMakueniFilterState<T>> getFilterModel(FilterForm form) {
                return new PropertyModel<>(form.getDefaultModel(), "tenderTitle");
            }
        });


//        columns.add(new PropertyColumn<T, String>(
//                new Model<>((new StringResourceModel(
//                        "title",
//                        ListAbstractTenderProcessMakueniEntity.this
//                )).getString()),
//                null, "tenderProcess.tender.iterator.next.tenderTitle"
//        ) {
//            @Override
//            public void populateItem(final Item<ICellPopulator<T>> item,
//                                     final String componentId,
//                                     final IModel<T> rowModel) {
//                final Tender tender = rowModel.getObject().getTenderProcess().getSingleTender();
//                if (tender != null) {
//                    item.add(new Label(componentId, tender.getTenderTitle()));
//                } else {
//                    item.add(new Label(componentId, "[[No Tender]]"));
//                }
//
//            }
//        });
    }
}
