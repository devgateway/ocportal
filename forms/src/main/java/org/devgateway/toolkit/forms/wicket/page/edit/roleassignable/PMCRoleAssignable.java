package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * @author mpostelnicu
 */
public interface PMCRoleAssignable extends ImplementationRoleAssignable {

    @Override
    default String getUserRole() {
        return SecurityConstants.Roles.ROLE_PMC_USER;
    }

    @Override
    default String getValidatorRole() {
        return SecurityConstants.Roles.ROLE_PMC_VALIDATOR;
    }
}
