package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.dao.categories.InspectionReportOutcome;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "inspectionReportForm")
public class InspectionReport extends AbstractAuthImplTenderProcessClientEntity {

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @ExcelExport(useTranslation = true)
    private String comments;

    @ExcelExport(name = "Satisfactory/Unsatisfactory")
    @ManyToOne
    private InspectionReportOutcome outcome;

    @ExcelExport(separateSheet = true, useTranslation = true, name = "Private Sector Requests")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PrivateSectorRequest> privateSectorRequests = new ArrayList<>();

    @ExcelExport(justExport = true, name = "Picture")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> picture = new HashSet<>();

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public InspectionReportOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(InspectionReportOutcome outcome) {
        this.outcome = outcome;
    }

    public List<PrivateSectorRequest> getPrivateSectorRequests() {
        return privateSectorRequests;
    }

    public void setPrivateSectorRequests(List<PrivateSectorRequest> privateSectorRequests) {
        this.privateSectorRequests = privateSectorRequests;
    }

    public Set<FileMetadata> getPicture() {
        return picture;
    }

    public void setPicture(Set<FileMetadata> picture) {
        this.picture = picture;
    }

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return Stream.concat(
                Stream.concat(
                        getFormDocs().stream(),
                        getPicture().stream()),
                getPrivateSectorRequests().stream().flatMap(r -> r.getUpload().stream())
        ).collect(Collectors.toCollection(HashSet::new));
    }
}
