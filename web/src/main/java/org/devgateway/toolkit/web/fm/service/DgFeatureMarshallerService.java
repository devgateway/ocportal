package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.DgFeature;

import java.util.Collection;

/**
 * @author mpostelnicu
 */
public interface DgFeatureMarshallerService {
    void marshall(String resourceLocation, Collection<DgFeature> dgFeatures);
}
