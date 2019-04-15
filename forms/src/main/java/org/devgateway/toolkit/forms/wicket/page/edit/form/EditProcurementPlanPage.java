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
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PlanItemPanel;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProcurementPlanPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
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

    private Select2ChoiceBootstrapFormComponent<FiscalYear> fiscalYear;

    public EditProcurementPlanPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = procurementPlanService;
        this.listPageClass = ListProcurementPlanPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "department", departmentService).required()
                .getField().add(uniqueProcurementPlan());
        fiscalYear = ComponentUtil.addSelect2ChoiceField(editForm, "fiscalYear", fiscalYearService);
        fiscalYear.required();

        editForm.add(new PlanItemPanel("planItems"));

        final FileInputBootstrapFormComponent procurementPlanDocs =
                new FileInputBootstrapFormComponent("procurementPlanDocs");
        procurementPlanDocs.required();
        editForm.add(procurementPlanDocs);

        ComponentUtil.addDateField(editForm, "approvedDate").required();
    }

    private IValidator<Department> uniqueProcurementPlan() {
        StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new UniqueProcurementPlanValidator(id.toLong(-1));
    }

    public class UniqueProcurementPlanValidator implements IValidator<Department> {
        private final Long id;

        public UniqueProcurementPlanValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<Department> validatable) {
            final Department department = validatable.getValue();
            final FiscalYear fiscalYearValue = fiscalYear.getModelObject();

            if (department != null && fiscalYearValue != null) {
                if (procurementPlanService
                        .countByDepartmentAndFiscalYearAndIdNot(department, fiscalYearValue, id) > 0) {
                    final ValidationError error = new ValidationError(getString("uniqueProcurementPlan"));
                    validatable.error(error);
                }
            }
        }
    }
}
