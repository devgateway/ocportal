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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;

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

    public static boolean hasAnyUserRoles(String... roles) {
        final Person p = getCurrentAuthenticatedPerson();
        if (p == null || p.getRoles() == null) {
            return false;
        }
        List<String> rList = Arrays.asList(roles);
        return p.getRoles().stream().anyMatch(r -> rList.contains(r.getAuthority()));
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

    public static String createURL(final HttpServletRequest request, final String resourcePath) {
        final int port = request.getServerPort();
        final StringBuilder result = new StringBuilder();

        result.append(request.getScheme())
                .append("://")
                .append(request.getServerName());

        if ((request.getScheme().equals("http") && port != 80)
                || (request.getScheme().equals("https") && port != 443)) {
            result.append(':')
                    .append(port);
        }

        result.append(request.getContextPath());

        if (resourcePath != null && resourcePath.length() > 0) {
            if (!resourcePath.startsWith("/")) {
                result.append("/");
            }
            result.append(resourcePath);
        }

        return result.toString();
    }
}
