package org.devgateway.toolkit.persistence.fm.service;

import org.devgateway.toolkit.persistence.fm.entity.FeatureConfig;
import org.devgateway.toolkit.persistence.fm.entity.UnchainedDgFeature;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.List;

/**
 * @author mpostelnicu
 */
public interface DgFeatureUnmarshallerService {
    List<UnchainedDgFeature> unmarshall(FeatureConfig featureConfig);

    @Validated
    UnchainedDgFeature validateUnchainedDgFeature(@Valid UnchainedDgFeature unchainedDgFeature);
}
