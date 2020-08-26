/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.web.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author mpostelnicu
 */
public final class SecurityConstants {

    public static final class JWTConstants {
        public static final long EXPIRATION_TIME = 604_800_000; //one week
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String AUTH_URL = "/api/login";
    }

    public static final class Roles {
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_PROCUREMENT_USER = "ROLE_PROCUREMENT_USER";
        public static final String ROLE_IMPLEMENTATION_USER = "ROLE_IMPLEMENTATION_USER";
        public static final String ROLE_PMC_VALIDATOR = "ROLE_PMC_VALIDATOR";
        public static final String ROLE_PMC_USER = "ROLE_PMC_USER";
        public static final String ROLE_PMC_ADMIN = "ROLE_PMC_ADMIN";
        public static final String ROLE_TECH_ADMIN_VALIDATOR = "ROLE_TECH_ADMIN_VALIDATOR";
        public static final String ROLE_ME_PAYMENT_VALIDATOR = "ROLE_ME_PAYMENT_VALIDATOR";
        public static final String ROLE_PROCUREMENT_VALIDATOR = "ROLE_PROCUREMENT_VALIDATOR";
        public static final String ROLE_PROCURING_ENTITY = "ROLE_PROCURING_ENTITY";
        
        public static final List<String> PMC_ROLES = Collections.unmodifiableList(Arrays.asList(Roles.ROLE_PMC_ADMIN,
                Roles.ROLE_PMC_USER, Roles.ROLE_PMC_VALIDATOR));
    }


    public static final class Action {
        public static final String EDIT = "EDIT";
        public static final String VIEW = "VIEW";
    }
}
