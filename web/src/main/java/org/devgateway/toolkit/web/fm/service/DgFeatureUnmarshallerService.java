package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.UnchainedDgFeature;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface DgFeatureUnmarshallerService {
    List<UnchainedDgFeature> unmarshall(String resourceLocation);
    List<String> getResources();

    @Validated
    UnchainedDgFeature validateUnchainedDgFeature(@Valid UnchainedDgFeature unchainedDgFeature);
}
