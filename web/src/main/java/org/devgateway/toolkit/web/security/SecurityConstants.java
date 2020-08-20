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
/**
 * 
 */
package org.devgateway.toolkit.web.security;

/**
 * @author mpostelnicu
 */
public final class SecurityConstants {

    public static final class JWTConstants {
        public static final long EXPIRATION_TIME = 900_000; // 15 mins
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
        public static final String ROLE_TECH_ADMIN_VALIDATOR = "ROLE_TECH_ADMIN_VALIDATOR";
        public static final String ROLE_ME_PAYMENT_VALIDATOR = "ROLE_ME_PAYMENT_VALIDATOR";
        public static final String ROLE_PROCUREMENT_VALIDATOR = "ROLE_PROCUREMENT_VALIDATOR";
        public static final String ROLE_PROCURING_ENTITY = "ROLE_PROCURING_ENTITY";
    }

    public static final class Action {
        public static final String EDIT = "EDIT";
        public static final String VIEW = "VIEW";
    }
}
