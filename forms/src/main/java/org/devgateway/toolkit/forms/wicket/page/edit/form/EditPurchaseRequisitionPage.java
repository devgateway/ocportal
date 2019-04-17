package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ItemDetailPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.persistence.service.category.StaffService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.wicketstuff.annotation.mount.MountPath;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisition")
public class EditPurchaseRequisitionPage extends EditAbstractMakueniFormPage<PurchaseRequisition> {
    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected StaffService staffService;

    @SpringBean
    protected ChargeAccountService chargeAccountService;

    public EditPurchaseRequisitionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = purchaseRequisitionService;
        this.listPageClass = ListPurchaseRequisitionPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();

        ComponentUtil.addIntegerTextField(editForm, "purchaseRequestNumber").required();
        ComponentUtil.addTextField(editForm, "title").required();

        ComponentUtil.addSelect2ChoiceField(editForm, "requestedBy", staffService).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "chargeAccount", chargeAccountService).required();
        ComponentUtil.addDateField(editForm, "requestApprovalDate").required();

        editForm.add(new ItemDetailPanel("itemDetails"));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent purchaseRequestDocs =
                new FileInputBootstrapFormComponent("purchaseRequestDocs");
        purchaseRequestDocs.required();
        editForm.add(purchaseRequestDocs);
    }

    @Override
    protected PurchaseRequisition newInstance() {
        final PurchaseRequisition purchaseRequisition = jpaService.newInstance();
        // purchaseRequisition.setProcurementPlan(procurementPlan);  // here we need to set the ProcurementPlan
        return purchaseRequisition;
    }
}
