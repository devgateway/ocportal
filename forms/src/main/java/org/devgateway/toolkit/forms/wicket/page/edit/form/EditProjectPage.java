package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProjectPage;
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

    public EditProjectPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = projectService;
        this.listPageClass = ListProjectPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();

        ComponentUtil.addSelect2ChoiceField(editForm, "cabinetPaper", cabinetPaperService).required();

        // TODO - add validation
        ComponentUtil.addTextField(editForm, "projectTitle").required()
                .getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);

        ComponentUtil.addDoubleField(editForm, "amountBudgeted");
        ComponentUtil.addDoubleField(editForm, "amountRequested");


        ComponentUtil.addIntegerTextField(editForm, "numberSubCounties")
                .getField().add(RangeValidator.range(1, 6));
        ComponentUtil.addIntegerTextField(editForm, "numberSubWards")
                .getField().add(RangeValidator.range(1, 30));

        ComponentUtil.addDateField(editForm, "approvedDate").required();
    }

    @Override
    protected Project newInstance() {
        final Project project = jpaService.newInstance();
        // project.setProcurementPlan(procurementPlan);  // here we need to set the ProcurementPlan
        return project;
    }

}
