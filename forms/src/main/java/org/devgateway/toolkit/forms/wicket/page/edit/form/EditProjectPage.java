package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProjectPage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Optional;

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

    public EditProjectPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = projectService;
        this.listPageClass = ListProjectPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }

    @Override
    protected Project newInstance() {
        final Project project = jpaService.newInstance();
        // project.setProcurementPlan(procurementPlan);  // here we need to set the ProcurementPlan
        return project;
    }

}
