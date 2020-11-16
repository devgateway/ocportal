package org.devgateway.toolkit.persistence.fm.service;

import org.devgateway.toolkit.persistence.fm.entity.DgFeature;

import java.util.Collection;

/**
 * @author mpostelnicu
 */
public interface DgFeatureMarshallerService {
    void marshall(String resourceLocation, Collection<DgFeature> dgFeatures);
}
