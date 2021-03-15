package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListProjectClosureHandoverPage;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.service.category.ProjectClosureHandoverService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/projectClosureHandover")
public class EditProjectClosureHandoverPage extends AbstractCategoryEditPage<ProjectClosureHandover> {

    @SpringBean
    private ProjectClosureHandoverService departmentService;

    public EditProjectClosureHandoverPage(final PageParameters parameters) {
        super(parameters);
        jpaService = departmentService;
        listPageClass = ListProjectClosureHandoverPage.class;
    }
}
