package org.devgateway.toolkit.forms.service;

import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.EditorValidatorRoleAssignable;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;

/**
 * @author idobre
 * @since 2019-04-23
 */
@Service
public class PermissionEntityRenderableService {

    public boolean isMatchingRightsOfEntity(EditorValidatorRoleAssignable page, Set<String> roles) {
        return roles.contains(page.getUserRole());
    }

    public String getAllowedAccess(final EditorValidatorRoleAssignable page, AbstractMakueniEntity entity) {
        return getAllowedAccess(page, entity.isNew(), entity.getDepartment());
    }

    public String getAllowedAccess(final EditorValidatorRoleAssignable page, boolean isNew, Department department) {
        final Set<String> roles = FormSecurityUtil.getStringRolesForCurrentPerson();
        Assert.notEmpty(roles, "Will not allow empty roles here!");

        // admins can always edit
        if (roles.contains(ROLE_ADMIN)) {
            return SecurityConstants.Action.EDIT;
        }

        // new forms can be added by validator/users in addition to admin types
        if (isNew && isMatchingRightsOfEntity(page, roles)) {
            return SecurityConstants.Action.EDIT;
        }

        // T should extend AbstractMakueniForm
        if (!isNew && isMatchingRightsOfEntity(page, roles)) {
            if (department == null) {
                return SecurityConstants.Action.VIEW;
            } else {
                final Set<Department> departments =
                        FormSecurityUtil.getCurrentAuthenticatedPerson().getDepartments();

                // users with different department should be redirected to view mode.
                if (departments.contains(department)) {
                    return SecurityConstants.Action.EDIT;
                } else {
                    return SecurityConstants.Action.VIEW;
                }
            }
        }

        return null;
    }
}
