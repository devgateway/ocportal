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
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/project")
public class EditProjectPage extends EditAbstractMakueniEntityPage<Project> {
    @SpringBean
    protected ProjectService projectService;

    @SpringBean
    private CabinetPaperService cabinetPaperService;
    
    public EditProjectPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = projectService;

        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && sessionMetadataService.getSessionPP() == null) {
            logger.warn("Something wrong happened since we are trying to add a new Project Entity "
                    + "without having a ProcurementPlan!");
            setResponsePage(StatusOverviewPage.class);
        }
    }
    
    @Override
    protected void onInitialize() {
        super.onInitialize();

        submitAndNext.setVisibilityAllowed(false);

        final TextFieldBootstrapFormComponent<String> projectTitle =
                ComponentUtil.addTextField(editForm, "projectTitle");
        projectTitle.required();
        projectTitle.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        projectTitle.getField().add(uniqueTitle());

        // filtered CabinetPapers based on form Procurement Plan
        final List<CabinetPaper> cabinetPapers = cabinetPaperService
                .findByProcurementPlan(editForm.getModelObject().getProcurementPlan());
        final Select2ChoiceBootstrapFormComponent cabinetPaper = new Select2ChoiceBootstrapFormComponent<>(
                "cabinetPaper", new GenericChoiceProvider<>(cabinetPapers));
        cabinetPaper.required();
        editForm.add(cabinetPaper);

        ComponentUtil.addBigDecimalField(editForm, "amountBudgeted")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO));
        ComponentUtil.addBigDecimalField(editForm, "amountRequested")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO));

        ComponentUtil.addIntegerTextField(editForm, "numberSubCounties")
                .getField().add(RangeValidator.range(1, 6));
        ComponentUtil.addIntegerTextField(editForm, "numberSubWards")
                .getField().add(RangeValidator.range(1, 30));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        saveTerminateButton.setVisibilityAllowed(false);
    }

    @Override
    protected Project newInstance() {
        final Project project = super.newInstance();
        project.setProcurementPlan(sessionMetadataService.getSessionPP());

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
