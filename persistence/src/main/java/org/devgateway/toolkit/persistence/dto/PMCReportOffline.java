package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PMCReportOffline implements Serializable {
    private Long id;
    private Long tenderId;
    private Boolean authorizePayment;
    private Set<Long> subcountyIds;
    private Set<Long> wardIds;
    private List<PMCMemberOffline> pmcMembers;
    private List<PMCNotesOffline> pmcNotes;
    private List<StatusChangedCommentOffline> statusComments;
    private Long pmcStatusId;
    private Set<Long> projectClosureHandoverIds;
    private String signatureNames;
    private Boolean acknowledgeSignature;
    private String status;
    private Date reportDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Boolean getAuthorizePayment() {
        return authorizePayment;
    }

    public void setAuthorizePayment(Boolean authorizePayment) {
        this.authorizePayment = authorizePayment;
    }

    public Set<Long> getSubcountyIds() {
        return subcountyIds;
    }

    public void setSubcountyIds(Set<Long> subcountyIds) {
        this.subcountyIds = subcountyIds;
    }

    public Set<Long> getWardIds() {
        return wardIds;
    }

    public void setWardIds(Set<Long> wardIds) {
        this.wardIds = wardIds;
    }

    public List<PMCMemberOffline> getPmcMembers() {
        return pmcMembers;
    }

    public void setPmcMembers(List<PMCMemberOffline> pmcMembers) {
        this.pmcMembers = pmcMembers;
    }

    public List<PMCNotesOffline> getPmcNotes() {
        return pmcNotes;
    }

    public void setPmcNotes(List<PMCNotesOffline> pmcNotes) {
        this.pmcNotes = pmcNotes;
    }

    public Long getPmcStatusId() {
        return pmcStatusId;
    }

    public void setPmcStatusId(Long pmcStatusId) {
        this.pmcStatusId = pmcStatusId;
    }

    public String getSignatureNames() {
        return signatureNames;
    }

    public void setSignatureNames(String signatureNames) {
        this.signatureNames = signatureNames;
    }

    public Boolean getAcknowledgeSignature() {
        return acknowledgeSignature;
    }

    public void setAcknowledgeSignature(Boolean acknowledgeSignature) {
        this.acknowledgeSignature = acknowledgeSignature;
    }

    public Set<Long> getProjectClosureHandoverIds() {
        return projectClosureHandoverIds;
    }

    public void setProjectClosureHandoverIds(Set<Long> projectClosureHandoverIds) {
        this.projectClosureHandoverIds = projectClosureHandoverIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StatusChangedCommentOffline> getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(List<StatusChangedCommentOffline> statusComments) {
        this.statusComments = statusComments;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
}