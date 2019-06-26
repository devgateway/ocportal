package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListSubcountyPage;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-06-26
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/subcounty")
public class EditSubcountyPage extends AbstractCategoryEditPage<Subcounty> {
    @SpringBean
    private SubcountyService subcountyService;

    public EditSubcountyPage(final PageParameters parameters) {
        super(parameters);
        jpaService = subcountyService;
        listPageClass = ListSubcountyPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}
