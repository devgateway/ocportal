package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractStatusAuditableEntity extends AbstractAuditableEntity implements Statusable {
    @NotNull
    @Audited
    private String status = DBConstants.Status.DRAFT;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderColumn(name = "index")
    @JsonIgnore
    protected List<StatusChangedComment> statusComments = new ArrayList<>();

    @Transient
    @JsonIgnore
    private String newStatusComment;

    @Transient
    @JsonIgnore
    private Boolean visibleStatusComments = false;

    @Transient
    @JsonIgnore
    private Boolean visibleStatusLabel = true;

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public List<StatusChangedComment> getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(final List<StatusChangedComment> statusComments) {
        this.statusComments = statusComments;
    }

    public String getNewStatusComment() {
        return newStatusComment;
    }

    public void setNewStatusComment(final String newStatusComment) {
        this.newStatusComment = newStatusComment;
    }

    public Boolean getVisibleStatusComments() {
        return visibleStatusComments;
    }

    public void setVisibleStatusComments(final Boolean visibleStatusComments) {
        this.visibleStatusComments = visibleStatusComments;
    }

    public Boolean getVisibleStatusLabel() {
        return visibleStatusLabel;
    }

    public void setVisibleStatusLabel(final Boolean visibleStatusLabel) {
        this.visibleStatusLabel = visibleStatusLabel;

    }
}
