package org.devgateway.toolkit.forms.wicket.page.edit;

import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.web.Constants;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.util.Set;

public interface PermissionAwareEntityRenderable<T extends AbstractStatusAuditableEntity> {

    default String getAllowedAccess(T e) {
        Set<String> roles = WebSecurityUtil.getStringRolesForCurrentPerson();

        if (!e.isNew() && (roles.contains(SecurityConstants.Roles.ROLE_ADMIN))) {
            return Constants.Action.EDIT;
        }

        //new activities can be added by main_ip editors in addition to admin types
        if ((roles.contains(SecurityConstants.Roles.ROLE_ADMIN) || roles.contains(
                SecurityConstants.Roles.ROLE_USER))
                && e.isNew()) {
            return Constants.Action.EDIT;
        }

//        if (roles.contains(SecurityConstants.Roles.ROLE_MAIN_IP_EDITOR)
//                || roles.contains(SecurityConstants.Roles.ROLE_MAIN_IP_VALIDATOR)) {
//            Organization o = WebSecurityUtil.getCurrentAuthenticatedPerson().getOrganization();
//            if (o.equals(e.getBoundOrganization())) {
//                return Constants.Action.EDIT;
//            }
//        }
//
//        if (roles.contains(SecurityConstants.Roles.ROLE_MAIN_IP_VIEWER)) {
//            Organization o = WebSecurityUtil.getCurrentAuthenticatedPerson().getOrganization();
//            if (o.equals(e.getBoundOrganization())) {
//                return Constants.Action.VIEW;
//            }
//        }

        return null;
    }
}
