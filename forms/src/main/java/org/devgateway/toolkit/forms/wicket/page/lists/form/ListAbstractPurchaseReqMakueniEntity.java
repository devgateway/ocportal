package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;

/**
 * @author idobre
 * @since 2019-05-23
 */
public abstract class ListAbstractPurchaseReqMakueniEntity<T extends AbstractPurchaseReqMakueniEntity>
        extends ListAbstractMakueniEntityPage<T> {

    public ListAbstractPurchaseReqMakueniEntity(final PageParameters parameters) {
        super(parameters);
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
}
