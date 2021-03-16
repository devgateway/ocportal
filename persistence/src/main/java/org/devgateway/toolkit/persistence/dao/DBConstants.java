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
package org.devgateway.toolkit.persistence.dao;

import com.google.common.collect.ImmutableMap;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class DBConstants {

    public static final String DATE_FORMAT = "dd/MM/YYYY";


    public static final String FROM_EMAIL = "noreply@opencontracting.makueni.go.ke";

    public static final String INSTANCE_NAME = "Makueni";


    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private DBConstants() {

    }

    public static final class SMSCommands {
        public static final String INFO = "INFO";
        public static final String REPORT = "REPORT";
    }

    public static final class Status {
        public static final String DRAFT = "DRAFT";
        public static final String SUBMITTED = "SUBMITTED";
        public static final String APPROVED = "APPROVED";
        public static final String TERMINATED = "TERMINATED";

        public static final String NOT_STARTED = "NOT_STARTED";

        public static final String[] ALL = {DRAFT, SUBMITTED, APPROVED, TERMINATED};
        public static final List<String> ALL_LIST = Collections.unmodifiableList(Arrays.asList(ALL));

        public static final List<String> EXPORTABLE = Collections.unmodifiableList(Arrays.asList(APPROVED, TERMINATED));
    }
    
    public static final class SupplierResponsiveness {
        public static final String FAIL = "Fail";
        public static final String PASS = "Pass";
        public static final String[] ALL = {FAIL, PASS};
        public static final List<String> ALL_LIST = Collections.unmodifiableList(Arrays.asList(ALL));
    }

    public static final class SupplierResponse {
        public static final String ACCEPTED = "Accepted";
        public static final String REJECTED = "Rejected";
        public static final String[] ALL = {ACCEPTED, REJECTED};
        public static final List<String> ALL_LIST = Collections.unmodifiableList(Arrays.asList(ALL));
    }



    public static final class Reports {
        public static final long DIRECT_PROCUREMENT_THRESHOLD = 500000;
    }

    public static final int MAX_DEFAULT_TEXT_LENGTH = 32000;
    public static final int STD_DEFAULT_TEXT_LENGTH = 255;
    public static final int MAX_DEFAULT_TEXT_LENGTH_ONE_LINE = 3000;
    public static final int MAX_DEFAULT_TEXT_AREA = 10000;

    // 1 digit, 1 lower, 1 upper, 1 symbol "@#$%", from 6 to 20
    // private static final String PASSWORD_PATTERN =
    // "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    // 1 digit, 1 caps letter, from 10 to 20
    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z]).{10,20})";
}
