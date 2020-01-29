package org.devgateway.toolkit.forms.wicket.page.edit.roleassignable;

import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * Defines entities that are editable by procurement roles
 */
public interface ProcurementRoleAssignable extends EditorValidatorRoleAssignable {

    @Override
    default String getUserRole() {
        return SecurityConstants.Roles.ROLE_PROCUREMENT_USER;
    }

    @Override
    default String getValidatorRole() {
        return SecurityConstants.Roles.ROLE_PROCUREMENT_VALIDATOR;
    }
}
