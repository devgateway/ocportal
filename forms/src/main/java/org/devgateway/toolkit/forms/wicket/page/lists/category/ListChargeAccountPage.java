package org.devgateway.toolkit.forms.wicket.page.lists.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.service.filterstate.category.ChargeAccountFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.category.EditChargeAccountPage;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/chargeaccounts")
public class ListChargeAccountPage extends AbstractListCategoryPage<ChargeAccount> {
    @SpringBean
    protected ChargeAccountService service;

    public ListChargeAccountPage(final PageParameters pageParameters) {
        super(pageParameters);

        this.jpaService = service;
        this.editPageClass = EditChargeAccountPage.class;
    }

    @Override
    protected void onInitialize() {
       super.onInitialize();
    }

    @Override
    public JpaFilterState<ChargeAccount> newFilterState() {
        return new ChargeAccountFilterState();
    }
}
