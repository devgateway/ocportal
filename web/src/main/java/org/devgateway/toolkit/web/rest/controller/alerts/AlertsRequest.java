package org.devgateway.toolkit.web.rest.controller.alerts;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author idobre
 * @since 23/08/2019
 */
public class AlertsRequest {
    @Schema(title = "Email address")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotNull
    private String email;

    @Schema(title = "Purchase Requisition identifier")
    private Long purchaseReqId;

    @Schema(title = "Departments identifiers")
    private Set<Long> departments;

    @Schema(title = "Items identifiers")
    private Set<Long> items;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Long getPurchaseReqId() {
        return purchaseReqId;
    }

    public void setPurchaseReqId(final Long purchaseReqId) {
        this.purchaseReqId = purchaseReqId;
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
