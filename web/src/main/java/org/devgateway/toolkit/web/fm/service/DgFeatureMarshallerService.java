package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.DgFeature;
import org.devgateway.toolkit.web.fm.entity.UnchainedDgFeature;

import java.util.Collection;
import java.util.List;

public interface DgFeatureMarshallerService {
    void marshall(String resourceLocation, Collection<DgFeature> dgFeatures);
}
