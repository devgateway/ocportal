package org.devgateway.toolkit.persistence.service.category;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface MetadataExportService {
    

    Map<String, List<Serializable>> getMetadataMap(Long userId);
}
