package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-04-01
 */
@MappedSuperclass
public abstract class AbstractMakueniEntity extends AbstractStatusAuditableEntity implements Labelable {

    private Date approvedDate;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> formDocs;


    /**
     * Gets direct children of current entity, that is the next form(s) that have to be filled in after this one is done
     * Most of the time this returns just one element, with the exception of {@link Project} where it will return
     * the {@link Project#getPurchaseRequisitions()}
     * This is used to revert downstream forms when upstream forms are reverted. So if u do not need this functionality
     * just return {@link Collections#emptyList()}
     * @return
     */
    public abstract Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities();

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Set<FileMetadata> getFormDocs() {
        return formDocs;
    }

    public void setFormDocs(final Set<FileMetadata> formDocs) {
        this.formDocs = formDocs;
    }
}
