package org.devgateway.toolkit.persistence.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;

/**
 * @author Octavian Ciubotaru
 */
public final class FiscalYearUtil {

    private FiscalYearUtil() {
    }

    public static int getFiscalYear(LocalDate localDate) {
        return localDate.get(ChronoField.YEAR) - (localDate.getMonthValue() < Month.JULY.getValue() ? 1 : 0);
    }
}
