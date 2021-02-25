package org.devgateway.toolkit.persistence.util;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * @author Octavian Ciubotaru
 */
public class FiscalYearUtilTest {

    @Test
    public void testFirstFiscalDay() {
        assertEquals(2021, FiscalYearUtil.getFiscalYear(LocalDate.of(2021, Month.JULY, 1)));
    }

    @Test
    public void testLastFiscalDay() {
        assertEquals(2020, FiscalYearUtil.getFiscalYear(LocalDate.of(2021, Month.JUNE, 30)));
    }

    @Test
    public void testFirstCalendarDay() {
        assertEquals(2020, FiscalYearUtil.getFiscalYear(LocalDate.of(2021, Month.JANUARY, 1)));
    }

    @Test
    public void testLastCalendarDay() {
        assertEquals(2021, FiscalYearUtil.getFiscalYear(LocalDate.of(2021, Month.DECEMBER, 31)));
    }
}