package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PlanItemPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProcurementPlanPage;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.WebSecurityUtil;
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

        // Non-Admins users should have the Department preselected when adding a new Procurement Plan
        if (WebSecurityUtil.isCurrentUserAdmin()) {
            ComponentUtil.addSelect2ChoiceField(editForm, "department", departmentService).required();
        } else {
            editForm.add(new GenericSleepFormComponent<>("department"));
        }

        ComponentUtil.addSelect2ChoiceField(editForm, "fiscalYear", fiscalYearService)
                .required()
                .getField().add(uniqueProcurementPlan());

        editForm.add(new PlanItemPanel("planItems"));

        final FileInputBootstrapFormComponent formDocs =
                new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);

        ComponentUtil.addDateField(editForm, "approvedDate").required();
    }

    @Override
    protected ProcurementPlan newInstance() {
        final ProcurementPlan procurementPlan = super.newInstance();

        // non-admin users should create a Procurement Plan only with their department
        if (!WebSecurityUtil.isCurrentUserAdmin()) {
            final Person person = WebSecurityUtil.getCurrentAuthenticatedPerson();
            procurementPlan.setDepartment(person.getDepartment());
        }

        return procurementPlan;
    }

    private IValidator<FiscalYear> uniqueProcurementPlan() {
        StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new UniqueProcurementPlanValidator(id.toLong(-1));
    }

    public class UniqueProcurementPlanValidator implements IValidator<FiscalYear> {
        private final Long id;

        public UniqueProcurementPlanValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<FiscalYear> validatable) {
            final FiscalYear fiscalYear = validatable.getValue();
            final Department department = editForm.getModelObject().getDepartment();

            if (department != null && fiscalYear != null) {
                if (procurementPlanService
                        .countByDepartmentAndFiscalYearAndIdNot(department, fiscalYear, id) > 0) {
                    final ValidationError error = new ValidationError(getString("uniqueProcurementPlan"));
                    validatable.error(error);
                }
            }
        }
    }
}
