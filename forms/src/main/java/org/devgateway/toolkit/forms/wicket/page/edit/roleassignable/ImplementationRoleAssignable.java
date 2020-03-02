package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * Defines entities that are editable by implementation roles
 */
public interface ImplementationRoleAssignable extends EditorValidatorRoleAssignable {

    @Override
    default String getUserRole() {
        return SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER;
    }
}
