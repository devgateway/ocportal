package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class NamedDateRange implements Serializable {
    private Date startDate;
    private Date endDate;

    //true for the 2nd interval in a fiscal range
    private boolean secondInterval = false;

    private Date fiscalYearStartDate;
    private Date fiscalYearEndDate;

    public Date getFiscalYearStartDate() {
        return fiscalYearStartDate;
    }

    public void setFiscalYearStartDate(Date fiscalYearStartDate) {
        this.fiscalYearStartDate = fiscalYearStartDate;
    }

    public Date getFiscalYearEndDate() {
        return fiscalYearEndDate;
    }

    public void setFiscalYearEndDate(Date fiscalYearEndDate) {
        this.fiscalYearEndDate = fiscalYearEndDate;
    }

    private String getMonthName(Date date) {
        return new SimpleDateFormat("MMM").format(date);
    }

    private String getYear(Date date) {
        return new SimpleDateFormat("yyyy").format(date);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isSecondInterval() {
        return secondInterval;
    }

    public void setSecondInterval(boolean secondInterval) {
        this.secondInterval = secondInterval;
    }

    public String getName() {
        return String.format("%s %s - %s %s",
                getMonthName(startDate), getYear(startDate), getMonthName(endDate), getYear(endDate));
    }

    @Override
    public String toString() {
        return getName();
    }
}