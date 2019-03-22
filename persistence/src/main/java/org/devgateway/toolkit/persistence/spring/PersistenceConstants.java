package org.devgateway.toolkit.persistence.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PersistenceConstants {

    private PersistenceConstants() {

    }

    public static final class Status {
        public static final String DRAFT = "DRAFT";
        public static final String SUBMITTED = "SUBMITTED";
        public static final String VALIDATED = "VALIDATED";

        public static final String[] ALL = {DRAFT, SUBMITTED, VALIDATED};
        public static final List<String> ALL_LIST = Collections.unmodifiableList(Arrays.asList(ALL));
    }

    public static final int BIGDECIMAL_SCALE = 15;
}
