package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-02
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/project")
public class EditProjectPage extends EditAbstractMakueniFormPage<Project> {
    @SpringBean
    protected ProjectService projectService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    private CabinetPaperService cabinetPaperService;
    
    private final ProcurementPlan procurementPlan;

    public EditProjectPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = projectService;

        this.procurementPlan = SessionUtil.getSessionPP();
        // TODO - check if it's new and without a PP... redirect to some page
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();        

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();

        // TODO - this should be filtered based on form Procurement Plan
        ComponentUtil.addSelect2ChoiceField(editForm, "cabinetPaper", cabinetPaperService).required();

        final TextFieldBootstrapFormComponent<String> projectTitle =
                ComponentUtil.addTextField(editForm, "projectTitle");
        projectTitle.required();
        projectTitle.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        projectTitle.getField().add(uniqueTitle());

        ComponentUtil.addDoubleField(editForm, "amountBudgeted").getField().add(RangeValidator.minimum(0.0));
        ComponentUtil.addDoubleField(editForm, "amountRequested").getField().add(RangeValidator.minimum(0.0));

        ComponentUtil.addIntegerTextField(editForm, "numberSubCounties")
                .getField().add(RangeValidator.range(1, 6));
        ComponentUtil.addIntegerTextField(editForm, "numberSubWards")
                .getField().add(RangeValidator.range(1, 30));

        ComponentUtil.addDateField(editForm, "approvedDate").required();
    }

    @Override
    protected Project newInstance() {
        final Project project = super.newInstance();
        project.setProcurementPlan(procurementPlan);
        return project;
    }

    private IValidator<String> uniqueTitle() {
        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new UniqueTitleValidator(id.toLong(-1));
    }

    public class UniqueTitleValidator implements IValidator<String> {
        private final Long id;

        public UniqueTitleValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String titleValue = validatable.getValue();
            final ProcurementPlan procurementPlan = editForm.getModelObject().getProcurementPlan();

            if (procurementPlan != null && titleValue != null) {
                if (projectService.countByProcurementPlanAndProjectTitleAndIdNot(procurementPlan, titleValue, id) > 0) {
                    final ValidationError error = new ValidationError(getString("uniqueTitle"));
                    validatable.error(error);
                }
            }
        }
    }
}
