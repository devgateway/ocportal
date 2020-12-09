package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form
public class InspectionReport extends AbstractAuthImplTenderProcessMakueniEntity {

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @ExcelExport(useTranslation = true)
    private String comments;

    @ExcelExport(separateSheet = true, useTranslation = true, name = "Private Sector Requests")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PrivateSectorRequest> privateSectorRequests = new ArrayList<>();

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<PrivateSectorRequest> getPrivateSectorRequests() {
        return privateSectorRequests;
    }

    public void setPrivateSectorRequests(List<PrivateSectorRequest> privateSectorRequests) {
        this.privateSectorRequests = privateSectorRequests;
    }
}
