package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListUnitPage;
import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.UnitService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 26/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/unit")
public class EditUnitPage extends AbstractCategoryEditPage<Unit> {
    @SpringBean
    private UnitService unitService;

    @SpringBean
    private SubcountyService subcountyService;

    public EditUnitPage(final PageParameters parameters) {
        super(parameters);
        jpaService = unitService;
        listPageClass = ListUnitPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }
}

