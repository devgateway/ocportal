package org.devgateway.toolkit.forms.wicket.page.edit.category;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.page.lists.category.ListChargeAccountPage;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount_;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/chargeaccount")
public class EditChargeAccountPage extends AbstractCategoryEditPage<ChargeAccount> {
    @SpringBean
    private ChargeAccountService service;

    public EditChargeAccountPage(final PageParameters parameters) {
        super(parameters);
        jpaService = service;
        listPageClass = ListChargeAccountPage.class;
    }
    
    private void addUniqueNameValidator() {
        label.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueName"),
                service::findOne, (o, v) -> (root, query, cb) -> cb.equal(root.get(
                        ChargeAccount_.label), v), editForm.getModel()
        ));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addUniqueNameValidator();
    }
}
