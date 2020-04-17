package org.devgateway.ocds.forms.wicket;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;

public class FormSecurityUtil {

    protected FormSecurityUtil() {
    }


    public static boolean rolesContainsAny(final Collection<String> roleSet, final String... roles) {
        for (final String role : roles) {
            if (roleSet.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean rolesContainsAny(final String... roles) {
        return rolesContainsAny(Objects.requireNonNull(getStringRolesForCurrentPerson()), roles);
    }

    public static boolean rolesContainsAll(final Collection<String> roleSet, final String... roles) {
        return roleSet.containsAll(Arrays.asList(roles));
    }

    public static boolean rolesContainsAll(final String... roles) {
        return rolesContainsAll(Objects.requireNonNull(getStringRolesForCurrentPerson()), roles);
    }

    /**
     * returns the principal object. In our case the principal should be
     * {@link Person}
     *
     * @return the principal or null
     * @see Principal
     */
    public static Person getCurrentAuthenticatedPerson() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        final Object principal = authentication.getPrincipal();
        if (principal instanceof Person) {
            return (Person) principal;
        }
        return null;
    }

    public static Set<String> getStringRolesForCurrentPerson() {
        if (!AbstractAuthenticatedWebSession.get().isSignedIn()) {
            return Collections.emptySet();
        }
        return AbstractAuthenticatedWebSession.get().getRoles();
    }

    public static boolean hasAnyUserRoles(String... roles) {
        List<String> rList = Arrays.asList(roles);
        return getStringRolesForCurrentPerson().stream().anyMatch(rList::contains);
    }

    public static boolean hasUserRole(String role) {
        return hasAnyUserRoles(role);
    }

    public static boolean isCurrentUserAdmin() {
        return hasUserRole(ROLE_ADMIN);
    }

    public static boolean isCurrentRoleOnlyUser(String userRole, String validatorRole) {
        if (hasAnyUserRoles(ROLE_ADMIN, validatorRole)) {
            return false;
        }
        return hasUserRole(userRole);
    }
}
