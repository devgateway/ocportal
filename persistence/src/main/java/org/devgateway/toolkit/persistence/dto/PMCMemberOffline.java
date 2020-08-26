package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

public class PMCMemberOffline implements Serializable {
    private Long id;
    private Long staffId;
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
