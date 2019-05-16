package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.ProjectFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-02
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/projects")
public class ListProjectPage extends ListAbstractMakueniEntityPage<Project> {
    @SpringBean
    protected ProjectService projectService;

    public ListProjectPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = projectService;
        this.editPageClass = EditProjectPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }


    @Override
    public JpaFilterState<Project> newFilterState() {
        return new ProjectFilterState();
    }
}
