package org.devgateway.toolkit.persistence.dao.flags;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(indexes = {@Index(columnList = "releaseId"), @Index(columnList = "flaggedDate")})
public class ReleaseFlagHistory extends GenericPersistable implements Serializable {

    protected String releaseId;

    @ElementCollection
    protected Set<String> flagged = new HashSet<>();

    protected ZonedDateTime flaggedDate;

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public Set<String> getFlagged() {
        return flagged;
    }

    public void setFlagged(Set<String> flagged) {
        this.flagged = flagged;
    }

    public ZonedDateTime getFlaggedDate() {
        return flaggedDate;
    }

    public void setFlaggedDate(ZonedDateTime flaggedDate) {
        this.flaggedDate = flaggedDate;
    }
}
