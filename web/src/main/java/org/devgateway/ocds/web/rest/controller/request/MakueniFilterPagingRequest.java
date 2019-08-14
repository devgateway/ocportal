package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.TreeSet;

/**
 * @author idobre
 * @since 2019-07-12
 */
public class MakueniFilterPagingRequest extends TextSearchRequest {
    @ApiModelProperty(value = "Department identifier")
    private Long department;

    @ApiModelProperty(value = "Fiscal Year identifier")
    private Long fiscalYear;

    @ApiModelProperty(value = "Item identifier")
    private Long item;

    @ApiModelProperty(value = "Subcounty identifier")
    private Long subcounty;

    @ApiModelProperty(value = "Ward identifier")
    private Long ward;

    @ApiModelProperty(value = "Tender min value")
    private BigDecimal min;

    @ApiModelProperty(value = "Tender max value")
    private BigDecimal max;

    @ApiModelProperty(value = "This parameter will filter the content based on year. " + "The minimum year allowed is "
            + MIN_REQ_YEAR + " and the maximum allowed is " + MAX_REQ_YEAR
            + ".It will check if the startDate and endDate are within the year range. "
            + "To check which fields are used to read start/endDate from, have a look at each endpoint definition.")
    private TreeSet<@Range(min = MIN_REQ_YEAR, max = MAX_REQ_YEAR) Integer> year;

    @ApiModelProperty(value = "This parameter will filter the content based on month. "
            + "The minimum month allowed is "
            + MIN_MONTH + " and the maximum allowed is " + MAX_MONTH
            + "This parameter does nothing if used without the year parameter, as filtering and aggregating by month "
            + "makes no sense without filtering by year. This parameter is also ignored when using multiple year "
            + "parameters, so it only works if and only if the year parameter has one value.")
    private TreeSet<@Range(min = MIN_MONTH, max = MAX_MONTH) Integer> month;

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final Long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Long getItem() {
        return item;
    }

    public void setItem(final Long item) {
        this.item = item;
    }

    public Long getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(final Long subcounty) {
        this.subcounty = subcounty;
    }

    public Long getWard() {
        return ward;
    }

    public void setWard(final Long ward) {
        this.ward = ward;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(final BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(final BigDecimal max) {
        this.max = max;
    }

    public TreeSet<Integer> getYear() {
        return year;
    }

    public void setYear(final TreeSet<Integer> year) {
        this.year = year;
    }

    public TreeSet<Integer> getMonth() {
        return month;
    }

    public void setMonth(final TreeSet<Integer> month) {
        this.month = month;
    }
}
