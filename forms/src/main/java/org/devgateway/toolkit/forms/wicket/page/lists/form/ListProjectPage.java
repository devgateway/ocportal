package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProjectFilterState;
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

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Department"),
                "procurementPlan.department", "procurementPlan.department", new ListModel(departments), dataTable));

        columns.add(new SelectFilteredBootstrapPropertyColumn<>(new Model<>("Fiscal Year"),
                "procurementPlan.fiscalYear", "procurementPlan.fiscalYear", new ListModel(fiscalYears), dataTable));

        columns.add(new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("projectTitle", ListProjectPage.this)).getString()),
                "projectTitle", "projectTitle"));

        columns.add(new PropertyColumn<>(
                new Model<>((new StringResourceModel("lastModifiedDate", ListProjectPage.this)).getString()),
                "lastModifiedDate", "lastModifiedDate.get"
        ));

        super.onInitialize();
    }


    @Override
    public JpaFilterState<Project> newFilterState() {
        return new ProjectFilterState();
    }
}
