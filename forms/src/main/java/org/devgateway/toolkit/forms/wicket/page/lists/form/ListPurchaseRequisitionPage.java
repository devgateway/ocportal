package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.PurchaseRequisitionFilterState;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.hibernate.Hibernate;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisitions")
public class ListPurchaseRequisitionPage extends ListAbstractMakueniEntityPage<PurchaseRequisition> {

    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    public ListPurchaseRequisitionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = purchaseRequisitionService;
        this.editPageClass = EditPurchaseRequisitionPage.class;
    }

    @Override
    protected void onInitialize() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("department", ListPurchaseRequisitionPage.this)).getString()),
                "project.procurementPlan.department", "project.procurementPlan.department",
                new ListModel(departments), dataTable));

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("fiscalYear", ListPurchaseRequisitionPage.this)).getString()),
                "project.procurementPlan.fiscalYear", "project.procurementPlan.fiscalYear",
                new ListModel(fiscalYears), dataTable));

        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("title", ListPurchaseRequisitionPage.this)).getString()),
                "title", "title"));

        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("lastModifiedDate", ListPurchaseRequisitionPage.this)).getString()),
                "lastModifiedDate", "lastModifiedDate.get"
        ));

        columns.add(new AbstractColumn<PurchaseRequisition, String>(
                new StringResourceModel("downloadFile", ListPurchaseRequisitionPage.this)) {
            @Override
            public void populateItem(final Item<ICellPopulator<PurchaseRequisition>> cellItem, final String componentId,
                                     final IModel<PurchaseRequisition> model) {
                final FileMetadata file = model.getObject().getFormDocs().stream().findFirst().orElse(null);
                if (file != null) {
                    Hibernate.initialize(file.getContent());
                    cellItem.add(new DownloadPanel(componentId, new Model(file)));
                }
            }
        });

        super.onInitialize();
    }


    @Override
    public JpaFilterState<PurchaseRequisition> newFilterState() {
        return new PurchaseRequisitionFilterState();
    }
}
