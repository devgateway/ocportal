package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.devgateway.toolkit.persistence.service.category.SupplierService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-23
 */
public abstract class ListAbstractPurchaseReqMakueniEntity<T extends AbstractPurchaseReqMakueniEntity>
        extends ListAbstractMakueniEntityPage<T> {

    protected final List<Supplier> awardees;

    @SpringBean
    private SupplierService supplierService;

    public ListAbstractPurchaseReqMakueniEntity(final PageParameters parameters) {
        super(parameters);

        this.awardees = supplierService.findAll();
    }

    @Override
    protected void onInitialize() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "purchaseRequisition.project.procurementPlan.department",
                "purchaseRequisition.project.procurementPlan.department",
                new ListModel(departments), dataTable));

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Fiscal Year"),
                "purchaseRequisition.project.procurementPlan.fiscalYear",
                "purchaseRequisition.project.procurementPlan.fiscalYear",
                new ListModel(fiscalYears), dataTable));

        columns.add(new PropertyColumn<>(new Model<>("Last Updated Date"),
                "lastModifiedDate", "lastModifiedDate.get"
        ));

        super.onInitialize();
    }

    protected void addAwardeeColumn() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>(
                        (new StringResourceModel("awardee", ListAbstractPurchaseReqMakueniEntity.this)).getString()),
                "awardee",
                "awardee",
                new ListModel(awardees), dataTable, false
        ));
    }

    protected void addTenderTitleColumn() {
        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("title", ListAbstractPurchaseReqMakueniEntity.this)).getString()),
                null,
                "purchaseRequisition.tender.iterator.next.tenderTitle"));
    }
}
