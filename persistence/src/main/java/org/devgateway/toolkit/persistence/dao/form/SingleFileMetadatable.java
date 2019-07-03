package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public interface SingleFileMetadatable {

    Set<FileMetadata> getFormDocs();

    @JsonIgnore
    default FileMetadata getFormDoc() {
        return ObjectUtils.isEmpty(getFormDocs()) ? null : getFormDocs().iterator().next();
    }
}
