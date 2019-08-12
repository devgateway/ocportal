package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

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
}
