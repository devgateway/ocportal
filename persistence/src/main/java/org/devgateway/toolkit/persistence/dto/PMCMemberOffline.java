package org.devgateway.toolkit.persistence.dto;

import jakarta.validation.constraints.NotNull;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;


import java.io.Serializable;

public class PMCMemberOffline implements Serializable {
    private Long id;
    @NotNull(groups = NonDraft.class)
    private Long staffId;
    @NotNull(groups = NonDraft.class)
    private Long designationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }
}
