/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListDesignationPage;
import org.devgateway.toolkit.persistence.dao.categories.Designation;
import org.devgateway.toolkit.persistence.service.category.DesignationService;
import org.wicketstuff.annotation.mount.MountPath;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_VALIDATOR;

/**
 * @author mpostlenicu
 *
 */
@AuthorizeInstantiation({ROLE_PMC_ADMIN, ROLE_PMC_VALIDATOR, ROLE_IMPLEMENTATION_USER })
@MountPath
public class EditDesignationPage extends AbstractCategoryEditPage<Designation> {
    @SpringBean
    private DesignationService service;


    public EditDesignationPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListDesignationPage.class;
    }
}
