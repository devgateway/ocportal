package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Collection;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "administratorReportForm")
public class AdministratorReport extends AbstractAuthImplTenderProcessClientEntity {

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return getFormDocs();
    }
}
