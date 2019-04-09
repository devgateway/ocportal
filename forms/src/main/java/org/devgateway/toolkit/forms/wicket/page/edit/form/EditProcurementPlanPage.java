package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PlanItemPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProcurementPlanPage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-02
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/procurementPlan")
public class EditProcurementPlanPage extends EditAbstractMakueniFormPage<ProcurementPlan> {
    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    public EditProcurementPlanPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = procurementPlanService;
        this.listPageClass = ListProcurementPlanPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "department", departmentService, false).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "fiscalYear", fiscalYearService, false).required();

        editForm.add(new PlanItemPanel("planItems"));

        final FileInputBootstrapFormComponent procurementPlanDocs =
                new FileInputBootstrapFormComponent("procurementPlanDocs");
        procurementPlanDocs.required();
        editForm.add(procurementPlanDocs);

        ComponentUtil.addDateField(editForm, "approvedDate", false).required();
    }
}
