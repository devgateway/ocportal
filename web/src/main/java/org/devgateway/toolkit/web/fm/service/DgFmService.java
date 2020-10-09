package org.devgateway.toolkit.web.fm.service;

import org.devgateway.toolkit.web.fm.entity.DgFeature;

public interface DgFmService {
    DgFeature getFeature(String featureName);
    Boolean hasFeature(String featureName);
    Boolean isFeatureEnabled(String featureName);
    Boolean isFeatureMandatory(String featureName);
    Boolean isFeatureVisible(String featureName);
    Boolean isFmActive();

    int featuresCount();

    /**
     * Creates the {@link DgFeature#getName()} by combining the parent FM name with the current feature name.
     * This is generally used by hierarchical component containers (like Wicket) to compile a hierarchical
     * FM name.
     *
     * @param parentFmName
     * @param featureName
     * @return
     */
    String getParentCombinedFmName(String parentFmName, String featureName);
}
