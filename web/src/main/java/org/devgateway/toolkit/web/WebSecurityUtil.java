/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.web;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WebSecurityUtil {

    protected WebSecurityUtil() {

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
        return rolesContainsAny(Objects.requireNonNull(WebSecurityUtil.getStringRolesForCurrentPerson()), roles);
    }


    public static boolean rolesContainsAll(final Collection<String> roleSet, final String... roles) {
        return roleSet.containsAll(Arrays.asList(roles));
    }

    public static boolean rolesContainsAll(final String... roles) {
        return rolesContainsAll(Objects.requireNonNull(WebSecurityUtil.getStringRolesForCurrentPerson()), roles);
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
        final Person person = getCurrentAuthenticatedPerson();
        if (person == null) {
            return null;
        }
        return person.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet());
    }

    /**
     * Returns true if the user has ROLE_ADMIN
     *
     * @param p
     * @return
     */
    public static boolean isUserAdmin(final Person p) {
        if (p == null || p.getRoles() == null) {
            return false;
        }
        for (final Role r : p.getRoles()) {
            if (r.getAuthority().equalsIgnoreCase(SecurityConstants.Roles.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCurrentUserAdmin() {
        final Person p = getCurrentAuthenticatedPerson();
        return isUserAdmin(p);
    }

    public static boolean isCurrentRoleUser() {
        final Person p = getCurrentAuthenticatedPerson();

        // check if we have more than 1 role
        if (p == null || p.getRoles() == null || p.getRoles().size() != 1) {
            return false;
        }
        if (p.getRoles().get(0).getAuthority().equalsIgnoreCase(SecurityConstants.Roles.ROLE_USER)) {
            return true;
        }
        return false;
    }
}
