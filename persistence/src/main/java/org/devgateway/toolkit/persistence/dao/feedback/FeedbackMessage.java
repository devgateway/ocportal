package org.devgateway.toolkit.persistence.dao.feedback;

import org.devgateway.toolkit.persistence.dao.AbstractChildExpandableAuditEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * @author mpostelnicu
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class FeedbackMessage extends AbstractChildExpandableAuditEntity<ReplyableFeedbackMessage> {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    @NotNull
    private String comment;

    private boolean visible = true;

    private boolean addedByPublic = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAddedByPublic() {
        return addedByPublic;
    }

    public boolean getAddedByPublic() {
        return addedByPublic;
    }

    public void setAddedByPublic(boolean addedByPublic) {
        this.addedByPublic = addedByPublic;
    }
}
