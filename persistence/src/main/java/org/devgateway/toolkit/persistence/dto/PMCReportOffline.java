package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.devgateway.toolkit.persistence.validator.groups.Draft;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PMCReportOffline implements Serializable {
    private Long id;
    private Long tenderId;

    private String socialSafeguards;
    private String emergingComplaints;
    private String pmcChallenges;

    @NotNull(groups = NonDraft.class)
    private Boolean authorizePayment;
    private Set<Long> subcountyIds = new HashSet<>();
    private Set<Long> wardIds = new HashSet<>();
    private List<PMCMemberOffline> pmcMembers = new ArrayList<>();

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

    private List<PMCNotesOffline> pmcNotes = new ArrayList<>();
    private List<StatusChangedCommentOffline> statusComments = new ArrayList<>();

    @NotNull(groups = NonDraft.class)
    private Long pmcStatusId;

    @NotEmpty(groups = NonDraft.class)
    private Set<Long> projectClosureHandoverIds = new HashSet<>();

    @NotBlank(groups = NonDraft.class)
    private String signatureNames;

    @AssertTrue(groups = NonDraft.class)
    @NotNull(groups = NonDraft.class)
    private Boolean acknowledgeSignature;

    @NotNull(groups = {NonDraft.class, Draft.class})
    private String status;
    private Boolean rejected;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(groups = NonDraft.class)
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

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getSocialSafeguards() {
        return socialSafeguards;
    }

    public void setSocialSafeguards(String socialSafeguards) {
        this.socialSafeguards = socialSafeguards;
    }

    public String getEmergingComplaints() {
        return emergingComplaints;
    }

    public void setEmergingComplaints(String emergingComplaints) {
        this.emergingComplaints = emergingComplaints;
    }

    public String getPmcChallenges() {
        return pmcChallenges;
    }

    public void setPmcChallenges(String pmcChallenges) {
        this.pmcChallenges = pmcChallenges;
    }
}
