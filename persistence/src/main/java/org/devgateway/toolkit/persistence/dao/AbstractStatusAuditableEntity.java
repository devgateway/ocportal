package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.spring.PersistenceConstants;
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
public abstract class AbstractStatusAuditableEntity extends AbstractAuditableEntity {

    @NotNull
    @Audited
    private String status = PersistenceConstants.Status.DRAFT;

    @Transient
    private String newStatusComment;

    @Transient
    private Boolean visibleStatusComments = false;

    @Transient
    private Boolean visibleStatusLabel = true;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderColumn(name = "index")
    @JsonIgnore
    private List<StatusChangedComment> statusComments = new ArrayList<>();

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
