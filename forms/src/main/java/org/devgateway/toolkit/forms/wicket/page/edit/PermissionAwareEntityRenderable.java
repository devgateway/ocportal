package org.devgateway.toolkit.forms.wicket.page.edit;

import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.util.Set;

public interface PermissionAwareEntityRenderable<T extends AbstractStatusAuditableEntity> {

    default String getAllowedAccess(T e) {
        final Set<String> roles = WebSecurityUtil.getStringRolesForCurrentPerson();

        if (!e.isNew() && (roles.contains(SecurityConstants.Roles.ROLE_ADMIN))) {
            return SecurityConstants.Action.EDIT;
        }

        // new forms can be added by editors in addition to admin types
        if ((roles.contains(SecurityConstants.Roles.ROLE_ADMIN)
                || roles.contains(SecurityConstants.Roles.ROLE_USER))
                && e.isNew()) {
            return SecurityConstants.Action.EDIT;
        }

        return null;
    }
}
