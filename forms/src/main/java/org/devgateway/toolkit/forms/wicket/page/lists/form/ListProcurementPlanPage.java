package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProcurementPlanFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-02
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/procurementPlans")
public class ListProcurementPlanPage extends ListAbstractMakueniEntityPage<ProcurementPlan> {
    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    public ListProcurementPlanPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = procurementPlanService;
        this.editPageClass = EditProcurementPlanPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


    @Override
    public JpaFilterState<ProcurementPlan> newFilterState() {
        return new ProcurementPlanFilterState();
    }
}
