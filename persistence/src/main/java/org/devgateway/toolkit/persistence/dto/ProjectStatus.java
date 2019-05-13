package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

/**
 * @author gmutuhu
 */
public class ProjectStatus implements Serializable {
    private static final long serialVersionUID = -4877317917505893733L;

    private Long id;

    private String projectTitle;

    private String projectStatus;

    private String tenderStatus;

    private String awardStatus;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(final String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(final String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getTenderStatus() {
        return tenderStatus;
    }

    public void setTenderStatus(final String tenderStatus) {
        this.tenderStatus = tenderStatus;
    }

    public String getAwardStatus() {
        return awardStatus;
    }

    public void setAwardStatus(final String awardStatus) {
        this.awardStatus = awardStatus;
    }

}
