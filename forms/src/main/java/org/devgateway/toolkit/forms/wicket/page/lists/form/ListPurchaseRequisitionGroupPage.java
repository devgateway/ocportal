package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPurchaseRequisitionGroupPage;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisitionGroup;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.PurchaseRequisitionGroupFilterState;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionGroupService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisitions")
public class ListPurchaseRequisitionGroupPage extends ListAbstractTenderProcessClientEntity<PurchaseRequisitionGroup> {
    @SpringBean
    private PurchaseRequisitionGroupService purchaseRequisitionGroupService;


    public ListPurchaseRequisitionGroupPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = purchaseRequisitionGroupService;
        this.editPageClass = EditPurchaseRequisitionGroupPage.class;
    }

    @Override
    protected void onInitialize() {
        attachFm("purchaseRequisitionList");
        super.onInitialize();
    }

    @Override
    public JpaFilterState<PurchaseRequisitionGroup> newFilterState() {
        return new PurchaseRequisitionGroupFilterState();
    }
}
