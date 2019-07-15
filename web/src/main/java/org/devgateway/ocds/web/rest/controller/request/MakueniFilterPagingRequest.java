package org.devgateway.ocds.web.rest.controller.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author idobre
 * @since 2019-07-12
 */
public class MakueniFilterPagingRequest extends TextSearchRequest {
    @ApiModelProperty(value = "Department identifier")
    Long department;

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }
}
