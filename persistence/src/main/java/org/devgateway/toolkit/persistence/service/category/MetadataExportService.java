package org.devgateway.toolkit.persistence.service.category;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.Map;

public interface MetadataExportService {

    Map<String, List<ImmutablePair<Long, String>>> getMetadataMap();
}
