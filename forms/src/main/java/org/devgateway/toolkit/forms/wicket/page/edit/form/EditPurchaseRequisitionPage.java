package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListPurchaseRequisitionPage;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisition")
public class EditPurchaseRequisitionPage extends EditAbstractMakueniFormPage<PurchaseRequisition> {
    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    public EditPurchaseRequisitionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = purchaseRequisitionService;
        this.listPageClass = ListPurchaseRequisitionPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }

    @Override
    protected PurchaseRequisition newInstance() {
        final PurchaseRequisition purchaseRequisition = jpaService.newInstance();
        // purchaseRequisition.setProcurementPlan(procurementPlan);  // here we need to set the ProcurementPlan
        return purchaseRequisition;
    }
}
