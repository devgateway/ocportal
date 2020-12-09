package org.devgateway.toolkit.persistence.fm.service;

import org.devgateway.toolkit.persistence.fm.entity.UnchainedDgFeature;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * @author mpostelnicu
 */
public interface DgFeatureUnmarshallerService {
    List<UnchainedDgFeature> unmarshall(String resourceLocation);

    List<String> getResources();

    @Validated
    UnchainedDgFeature validateUnchainedDgFeature(@Valid UnchainedDgFeature unchainedDgFeature);
}
