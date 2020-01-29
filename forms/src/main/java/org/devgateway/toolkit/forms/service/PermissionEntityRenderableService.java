package org.devgateway.toolkit.forms.service;

import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAbstractMakueniEntityPage;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlanAttachable;
import org.devgateway.toolkit.persistence.dao.form.abstracted.ImplementationEditable;
import org.devgateway.toolkit.persistence.dao.form.abstracted.ProcurementEditable;
import org.devgateway.toolkit.web.WebSecurityUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PROCUREMENT_USER;

/**
 * @author idobre
 * @since 2019-04-23
 */
@Service
public class PermissionEntityRenderableService {

    public boolean isMatchingRightsOfEntity(AbstractStatusAuditableEntity entity, Set<String> roles) {
        if (entity instanceof ProcurementEditable) {
            return roles.contains(ROLE_PROCUREMENT_USER);
        }
        if (entity instanceof ImplementationEditable) {
            return roles.contains(ROLE_IMPLEMENTATION_USER);
        }
        return false;
    }

    public String getAllowedAccess(final EditAbstractMakueniEntityPage<?> page, boolean isNew, Department department) {
        final Set<String> roles = WebSecurityUtil.getStringRolesForCurrentPerson();
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
            if (entity instanceof AbstractMakueniEntity) {
                final ProcurementPlan procurementPlan;

                if (entity instanceof ProcurementPlanAttachable) {
                    procurementPlan = ((ProcurementPlanAttachable) entity).getProcurementPlan();
                } else if (entity instanceof ProcurementPlan) {
                    procurementPlan = (ProcurementPlan) entity;
                } else {
                    return null;
                }

                if (procurementPlan == null || procurementPlan.getDepartment() == null) {
                    return SecurityConstants.Action.VIEW;
                } else {
                    final Set<Department> departments =
                            WebSecurityUtil.getCurrentAuthenticatedPerson().getDepartments();
                    final Department formDepartment = procurementPlan.getDepartment();

                    // users with different department should be redirected to view mode.
                    if (departments.contains(formDepartment)) {
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
