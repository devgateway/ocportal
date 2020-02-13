package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.SimpleDateProperyColumn;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.service.filterstate.form.AbstractImplTenderProcessFilterState;

/**
 * @author mpostelnicu
 */
public abstract class ListAbstractImplTenderProcessMakueniEntity<T extends AbstractImplTenderProcessMakueniEntity>
        extends ListAbstractTenderProcessMakueniEntity<T> {


    public ListAbstractImplTenderProcessMakueniEntity(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addAwardeeColumn() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<T, AbstractImplTenderProcessFilterState<T>, String>(
                new Model<>(
                        (new StringResourceModel(
                                "awardee",
                                ListAbstractImplTenderProcessMakueniEntity.this
                        )).getString()),
                "tenderProcess.contract.awardee",
                "tenderProcess.singleContract.awardee",
                new ListModel(awardees), dataTable, false
        ) {
            @Override
            protected IModel<AbstractImplTenderProcessFilterState<T>> getFilterModel(FilterForm form) {
                return new PropertyModel<>(form.getDefaultModel(), "awardee");
            }
        });


    }

    protected void addAuthorizePaymentColumn() {
        columns.add(new PropertyColumn<>(new Model<>(
                (new StringResourceModel(
                        "authorizePayment",
                        ListAbstractImplTenderProcessMakueniEntity.this
                )).getString()), "authorizePayment", "authorizePayment"));
    }

    @Override
    protected void onInitialize() {
        addAwardeeColumn();

        columns.add(new SimpleDateProperyColumn<>(new StringResourceModel(
                "approvedDate",
                ListAbstractImplTenderProcessMakueniEntity.this),
                "approvedDate", "approvedDate",
                t -> SimpleDateProperyColumn.convertDateToZonedDateTime(t.getApprovedDate())
        ));

        super.onInitialize();
    }
}
