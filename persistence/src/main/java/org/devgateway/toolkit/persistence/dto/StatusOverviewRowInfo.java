package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

/**
 * @author gmutuhu
 */
public class StatusOverviewRowInfo implements Serializable {
    private static final long serialVersionUID = -4877317917505893733L;

    private Long id;

    private String title;

    private String projectStatus;

    private String tenderProcessStatus;

    private String awardProcessStatus;

    private String implementationStatus;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(final String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getTenderProcessStatus() {
        return tenderProcessStatus;
    }

    public void setTenderProcessStatus(final String tenderProcessStatus) {
        this.tenderProcessStatus = tenderProcessStatus;
    }

    public String getAwardProcessStatus() {
        return awardProcessStatus;
    }

    public void setAwardProcessStatus(final String awardProcessStatus) {
        this.awardProcessStatus = awardProcessStatus;
    }

    public String getImplementationStatus() {
        return implementationStatus;
    }

    public void setImplementationStatus(String implementationStatus) {
        this.implementationStatus = implementationStatus;
    }
}
