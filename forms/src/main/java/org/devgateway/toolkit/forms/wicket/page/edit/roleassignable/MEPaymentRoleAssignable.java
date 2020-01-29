package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * @author mpostelnicu
 */
public interface MEPaymentRoleAssignable extends ImplementationRoleAssignable {

    @Override
    default String getValidatorRole() {
        return SecurityConstants.Roles.ROLE_ME_PAYMENT_VALIDATOR;
    }
}
