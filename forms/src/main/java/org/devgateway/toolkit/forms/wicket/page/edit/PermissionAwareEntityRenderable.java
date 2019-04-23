package org.devgateway.toolkit.forms.wicket.page.edit;

import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniForm;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.util.Set;

public interface PermissionAwareEntityRenderable<T extends AbstractStatusAuditableEntity> {

    default String getAllowedAccess(final T e) {
        final Set<String> roles = WebSecurityUtil.getStringRolesForCurrentPerson();

        // admins can edit
        if (roles.contains(SecurityConstants.Roles.ROLE_ADMIN)) {
            return SecurityConstants.Action.EDIT;
        }

        // new forms can be added by validator/users in addition to admin types
        if (e.isNew() && (roles.contains(SecurityConstants.Roles.ROLE_VALIDATOR)
                || roles.contains(SecurityConstants.Roles.ROLE_USER))) {
            return SecurityConstants.Action.EDIT;
        }

        // T should extend AbstractMakueniForm
        if (!e.isNew() && (roles.contains(SecurityConstants.Roles.ROLE_VALIDATOR)
                || roles.contains(SecurityConstants.Roles.ROLE_USER))) {
            if (e instanceof AbstractMakueniForm) {
                final ProcurementPlan procurementPlan = ((AbstractMakueniForm) e).getProcurementPlan();

                if (procurementPlan == null || procurementPlan.getDepartment() == null) {
                    return SecurityConstants.Action.VIEW;
                } else {
                    final Department department = WebSecurityUtil.getCurrentAuthenticatedPerson().getDepartment();
                    final Department formDepartment = procurementPlan.getDepartment();

                    // users with different department should be redirected to view mode.
                    if (department.equals(formDepartment)) {
                        return SecurityConstants.Action.EDIT;
                    } else {
                        return SecurityConstants.Action.VIEW;
                    }
                }
            }
        }

        return null;
    }
}
