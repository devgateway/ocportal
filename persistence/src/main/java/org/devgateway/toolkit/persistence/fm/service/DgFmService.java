package org.devgateway.toolkit.persistence.fm.service;

import org.devgateway.toolkit.persistence.fm.entity.DgFeature;
import org.devgateway.toolkit.persistence.fm.entity.FeatureConfig;

import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu
 */
public interface DgFmService {

    List<DgFeature> getFeaturesByPrefix(String featureName);

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

    Set<FeatureConfig> getFeatureConfigs();

    void addOrReplaceFeatureConfig(FeatureConfig featureConfig);
}
