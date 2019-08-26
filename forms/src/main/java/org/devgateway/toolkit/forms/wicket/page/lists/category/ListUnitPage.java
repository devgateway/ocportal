package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditUnitPage;
import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.service.category.UnitService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.category.UnitFilterState;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 26/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/units")
public class ListUnitPage extends AbstractListCategoryPage<Unit> {
    @SpringBean
    private UnitService unitService;

    public ListUnitPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = unitService;
        this.editPageClass = EditUnitPage.class;
    }

    @Override
    protected void onInitialize() {

        super.onInitialize();
    }

    @Override
    public JpaFilterState<Unit> newFilterState() {
        return new UnitFilterState();
    }
}
