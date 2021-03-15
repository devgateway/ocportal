package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditProjectClosureHandoverPage;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.service.category.ProjectClosureHandoverService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/projectClosureHandoverList")
public class ListProjectClosureHandoverPage extends AbstractListCategoryPage<ProjectClosureHandover> {

    @SpringBean
    protected ProjectClosureHandoverService projectClosureHandoverService;

    public ListProjectClosureHandoverPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = projectClosureHandoverService;
        this.editPageClass = EditProjectClosureHandoverPage.class;
    }
}
