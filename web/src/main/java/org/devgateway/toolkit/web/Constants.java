package org.devgateway.toolkit.web;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author mpostelnicu
 */
public final class Constants {
    private Constants() {
    }

    public static class ContentType {
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        public static final String WPS_XLSX = "application/wps-office.xlsx";
        public static final Set<String> ALL_XLSX = ImmutableSet.of(XLSX, WPS_XLSX);
    }
}
