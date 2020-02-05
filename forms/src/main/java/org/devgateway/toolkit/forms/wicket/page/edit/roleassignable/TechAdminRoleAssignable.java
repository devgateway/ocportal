package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * @author mpostelnicu
 */
public interface TechAdminRoleAssignable extends ImplementationRoleAssignable {

    @Override
    default String getValidatorRole() {
        return SecurityConstants.Roles.ROLE_TECH_ADMIN_VALIDATOR;
    }
}
