package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.dao.categories.PMCStatus;
import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "pmcReportForm")
public class PMCReport extends AbstractAuthImplTenderProcessMakueniEntity {

    @ExcelExport(justExport = true, useTranslation = true, name = "Sub-Counties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Subcounty> subcounties;

    @ExcelExport(justExport = true, useTranslation = true, name = "Wards")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Ward> wards = new ArrayList<>();

    @ExcelExport(name = "PMC Members", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PMCMember> pmcMembers = new ArrayList<>();

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @ExcelExport(useTranslation = true, name = "Social Safeguards")
    private String socialSafeguards;

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @ExcelExport(useTranslation = true, name = "Emerging Complaints")
    private String emergingComplaints;

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @ExcelExport(useTranslation = true, name = "PMC Challenges")
    private String pmcChallenges;

    @ExcelExport(name = "PMC Notes", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PMCNotes> pmcNotes = new ArrayList<>();

    @ExcelExport(name = "PMC Status")
    @ManyToOne
    private PMCStatus pmcStatus;

    @ExcelExport(justExport = true, useTranslation = true, name = "Project Closure Handover")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<ProjectClosureHandover> projectClosureHandover;

    private String signatureNames;

    private Boolean acknowledgeSignature;

    private Boolean rejected = false;

    public List<Subcounty> getSubcounties() {
        return subcounties;
    }

    public void setSubcounties(List<Subcounty> subcounties) {
        this.subcounties = subcounties;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }

    public List<PMCMember> getPmcMembers() {
        return pmcMembers;
    }

    public void setPmcMembers(List<PMCMember> pmcMembers) {
        this.pmcMembers = pmcMembers;
    }

    public PMCStatus getPmcStatus() {
        return pmcStatus;
    }

    public void setPmcStatus(PMCStatus pmcStatus) {
        this.pmcStatus = pmcStatus;
    }

    public List<ProjectClosureHandover> getProjectClosureHandover() {
        return projectClosureHandover;
    }

    public void setProjectClosureHandover(List<ProjectClosureHandover> projectClosureHandover) {
        this.projectClosureHandover = projectClosureHandover;
    }

    public List<PMCNotes> getPmcNotes() {
        return pmcNotes;
    }

    public void setPmcNotes(List<PMCNotes> pmcNotes) {
        this.pmcNotes = pmcNotes;
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

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
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

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return getFormDocs();
    }
}
