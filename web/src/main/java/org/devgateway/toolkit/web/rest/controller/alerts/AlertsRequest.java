package org.devgateway.toolkit.web.rest.controller.alerts;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author idobre
 * @since 23/08/2019
 */
public class AlertsRequest {
    @ApiModelProperty(value = "Email address")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotNull
    private String email;

    @ApiModelProperty(value = "Departments identifiers")
    private Set<Long> departments;

    @ApiModelProperty(value = "Items identifiers")
    private Set<Long> items;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Set<Long> getDepartments() {
        return departments;
    }

    public void setDepartments(final Set<Long> departments) {
        this.departments = departments;
    }

    public Set<Long> getItems() {
        return items;
    }

    public void setItems(final Set<Long> items) {
        this.items = items;
    }
}
