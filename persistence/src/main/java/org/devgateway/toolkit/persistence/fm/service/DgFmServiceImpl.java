package org.devgateway.toolkit.persistence.fm.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.devgateway.toolkit.persistence.fm.DgFmProperties;
import org.devgateway.toolkit.persistence.fm.FmConstants;
import org.devgateway.toolkit.persistence.fm.FmReconfiguredEvent;
import org.devgateway.toolkit.persistence.fm.entity.DgFeature;
import org.devgateway.toolkit.persistence.fm.entity.FeatureConfig;
import org.devgateway.toolkit.persistence.fm.entity.UnchainedDgFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mpostelnicu
 */
@Service
@Validated
public class DgFmServiceImpl implements DgFmService {

    private static final Logger logger = LoggerFactory.getLogger(DgFmServiceImpl.class);

    @Autowired
    private DgFeatureUnmarshallerService featureUnmarshallerService;

    @Autowired
    private DgFeatureMarshallerService dgFeatureMarshallerService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Map<String, UnchainedDgFeature> unchainedFeatures;

    private Map<String, DgFeature> features;

    private Set<String> featuresInUse;

    @Autowired
    private DgFmProperties fmProperties;

    @Autowired
    private CacheManager cacheManager;

    private final ConcurrentSkipListSet<FeatureConfig> featureConfigs = new ConcurrentSkipListSet<>();

    private final Set<FeatureConfig> unmodifiableFeatureConfigs = Collections.unmodifiableSet(featureConfigs);

    public DgFeature createFeatureWithDefaults(String fmName) {
        DgFeature dgFeature = new DgFeature();
        dgFeature.setName(fmName);
        dgFeature.setEnabled(FmConstants.DEFAULT_ENABLED);
        dgFeature.setMandatory(FmConstants.DEFAULT_MANDATORY);
        dgFeature.setVisible(FmConstants.DEFAULT_VISIBLE);
        return dgFeature;
    }

    @PostConstruct
    public void init() {
        if (fmProperties.isActive()) {
            featuresInUse = ConcurrentHashMap.newKeySet();
            hydrateUnchainedFeatures();
            Map<String, DgFeature> mutableChainedFeatures = new ConcurrentHashMap<>();
            chainFeatures(mutableChainedFeatures);
            Map<String, DgFeature> projectedFeatures = new ConcurrentHashMap<>();
            projectProperties(projectedFeatures, mutableChainedFeatures);
            features = projectedFeatures;
            logger.info("FM: Done initializing the Feature Manager.");
        } else {
            logger.info("FM: Feature Manager is disabled.");
        }
    }

    private void emitProjectedFm() {
        if (fmProperties.isEmitProjected()) {
            dgFeatureMarshallerService.marshall("_projectedFm.yml", features.values()
                    .stream().sorted(Comparator.comparing(DgFeature::getName)).collect(Collectors.toList()));
        }
    }

    @PreDestroy
    public void decommission() {
        if (!fmProperties.isActive()) {
            return;
        }
        emitProjectedFm();
        if (fmProperties.isPrintFeaturesInUseOnExit()) {
            logger.info(String.format("FM: Features that have been used during this session: %s ",
                    featuresInUse.toString()));
        }
    }

    @Override
    public Collection<DgFeature> getFeatures() {
        return features.values();
    }

    @Override
    public List<DgFeature> getFeaturesByPrefix(String prefixName) {
        return features.keySet().stream().filter(k -> k.startsWith(prefixName))
                .map(this::getFeature).collect(Collectors.toList());
    }

    @Override
    public DgFeature getFeature(String featureName) {
        if (!fmProperties.isActive()) {
            return null;
        }
        if (featureName == null) {
            return createFeatureWithDefaults(null);
        }
        logger.debug(String.format("FM: Querying feature %s", featureName));
        DgFeature dgFeature = features.get(featureName);
        if (dgFeature == null) {
            if (fmProperties.isDefaultsForMissingFeatures()) {
                dgFeature = createFeatureWithDefaults(featureName);
                features.put(featureName, dgFeature);
            } else {
                throw new RuntimeException(String.format("Unknown feature with name %s", featureName));
            }
        }
        featuresInUse.add(featureName);
        return dgFeature;
    }

    @Override
    public Boolean hasFeature(String featureName) {
        if (!fmProperties.isActive()) {
            return null;
        }
        logger.debug(String.format("FM: Checking existence of feature %s", featureName));
        return features.containsKey(featureName);
    }

    @Override
    public Boolean isFeatureEnabled(String featureName) {
        logger.debug(String.format("FM: Querying enabled for feature %s", featureName));
        return getFeature(featureName).getEnabled();
    }

    @Override
    public Boolean isFeatureVisible(String featureName) {
        logger.debug(String.format("FM: Querying visible for feature %s", featureName));
        return getFeature(featureName).getVisible();
    }

    @Override
    public Boolean isFmActive() {
        return fmProperties.isActive();
    }

    @Override
    public int featuresCount() {
        if (!fmProperties.isActive()) {
            return 0;
        }
        return features.size();
    }


    @Override
    public Boolean isFeatureMandatory(String featureName) {
        logger.debug(String.format("FM: Querying mandatory for feature %s", featureName));
        return getFeature(featureName).getMandatory();
    }

    @Override
    public String getParentCombinedFmName(String parentFmName, String featureName) {
        if (parentFmName == null) {
            return null;
        }
        if (featureName == null) {
            throw new RuntimeException("Cannot create a parent combined fmName for a null fmName");
        }
        return parentFmName + "." + featureName;
    }

    private void projectProperties(Map<String, DgFeature> projectedFeatures, Map<String, DgFeature> features) {
        logger.debug("FM: Projecting properties for all features");
        features.values().forEach(f -> projectProperties(projectedFeatures, f));
        logger.debug("FM: Projected properties for all features");
    }

    private void projectProperties(Map<String, DgFeature> projectedFeatures,
                                   DgFeature feature) {
        if (projectedFeatures.containsKey(feature.getName())) {
            return;
        }

        feature.getChainedMixins().forEach(m -> projectProperties(projectedFeatures, m));
        feature.getChainedEnabledDeps().forEach(m -> projectProperties(projectedFeatures, m));
        feature.getChainedVisibleDeps().forEach(m -> projectProperties(projectedFeatures, m));
        feature.getChainedMandatoryDeps().forEach(m -> projectProperties(projectedFeatures, m));
        feature.getChainedSoftDeps().forEach(m -> projectProperties(projectedFeatures, m));

        feature.freeze();

        logger.debug(String.format("FM: Projecting properties for feature %s", feature));
        projectChainedMixins(feature);
        projectChainedVisibleDeps(feature);
        projectChainedEnabledDeps(feature);
        projectChainedMandatoryDeps(feature);
        projectChainedSoftDeps(feature);
        projectEnabled(feature);
        projectVisible(feature);
        projectMandatory(feature);
        projectedFeatures.put(feature.getName(), feature);
        logger.debug(String.format("FM: Projected properties for feature %s", feature));
    }

    private void projectChainedMixins(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained mixins for feature %s", feature));
        feature.getChainedMixins().addAll(getHierarchyChainedMixins(feature));
        logger.debug(String.format("FM: projected chained mixins for feature %s", feature));
    }

    private void projectEnabled(DgFeature feature) {
        logger.debug(String.format("FM: Projecting enabled for feature %s", feature));
        feature.setEnabled(feature.getEnabled()
                && feature.getChainedMixins().stream().map(DgFeature::getEnabled).reduce(Boolean::logicalAnd)
                .orElse(FmConstants.DEFAULT_ENABLED)
                && feature.getChainedEnabledDeps().stream().map(DgFeature::getEnabled).reduce(Boolean::logicalAnd)
                .orElse(FmConstants.DEFAULT_VISIBLE));
        logger.debug(String.format("FM: Projected enabled for feature %s", feature));
    }

    private void projectVisible(DgFeature feature) {
        logger.debug(String.format("FM: Projecting visible for feature %s", feature));
        feature.setVisible(feature.getVisible() && feature
                .getChainedMixins().stream().map(DgFeature::getVisible).reduce(Boolean::logicalAnd)
                .orElse(FmConstants.DEFAULT_VISIBLE)
                && feature.getChainedVisibleDeps().stream().map(DgFeature::getVisible).reduce(Boolean::logicalAnd)
                .orElse(FmConstants.DEFAULT_VISIBLE));
        logger.debug(String.format("FM: Projected visible for feature %s", feature));
    }

    private void projectMandatory(DgFeature feature) {
        logger.debug(String.format("FM: Projecting mandatory for feature %s", feature));
        feature.setMandatory(feature.getMandatory() || feature
                .getChainedMixins().stream().map(DgFeature::getMandatory).reduce(Boolean::logicalOr)
                .orElse(FmConstants.DEFAULT_MANDATORY)
                || feature.getChainedMandatoryDeps().stream().map(DgFeature::getMandatory).reduce(Boolean::logicalOr)
                .orElse(FmConstants.DEFAULT_MANDATORY)
        );
        logger.debug(String.format("FM: Projected mandatory for feature %s", feature));
    }

    private void projectChainedVisibleDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained visible deps for feature %s", feature));
        feature.getChainedVisibleDeps().addAll(getHierarchyChainedVisibleDeps(feature));
        logger.debug(String.format("FM: Projected chained visible deps for feature %s", feature));
    }


    private void projectChainedMandatoryDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained mandatory deps for feature %s", feature));
        feature.getChainedVisibleDeps().addAll(getHierarchyChainedMandatoryDeps(feature));
        logger.debug(String.format("FM: Projected chained mandatory deps for feature %s", feature));
    }

    private void projectChainedEnabledDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained enabled deps for feature %s", feature));
        feature.getChainedEnabledDeps().addAll(getHierarchyChainedEnabledDeps(feature));
        logger.debug(String.format("FM: Projected chained enabled deps for feature %s", feature));
    }

    private void projectChainedSoftDeps(DgFeature feature) {
        logger.debug(String.format("FM: Projecting chained soft deps for feature %s", feature));
        feature.getChainedSoftDeps().addAll(getHierarchyChainedSoftDeps(feature));
        logger.debug(String.format("FM: Projected chained soft deps for feature %s", feature));
    }

    private Set<DgFeature> getHierarchyChainedMixins(DgFeature feature) {
        return feature.getChainedMixins().stream().flatMap(f -> getHierarchyChainedMixins(f).stream())
                .collect(Collectors.toSet());
    }

    public Set<DgFeature> getHierarchyChainedVisibleDeps(DgFeature feature) {
        return feature.getChainedVisibleDeps().stream().flatMap(f -> getHierarchyChainedVisibleDeps(f).stream())
                .collect(Collectors.toSet());
    }

    public Set<DgFeature> getHierarchyChainedMandatoryDeps(DgFeature feature) {
        return feature.getChainedMandatoryDeps().stream().flatMap(f -> getHierarchyChainedMandatoryDeps(f).stream())
                .collect(Collectors.toSet());
    }

    public Set<DgFeature> getHierarchyChainedEnabledDeps(DgFeature feature) {
        return feature.getChainedEnabledDeps().stream().flatMap(f -> getHierarchyChainedEnabledDeps(f).stream())
                .collect(Collectors.toSet());
    }

    private Set<DgFeature> getHierarchyChainedSoftDeps(DgFeature feature) {
        return feature.getChainedSoftDeps().stream().flatMap(f -> getHierarchyChainedSoftDeps(f).stream())
                .collect(Collectors.toSet());
    }

    public void hydrateUnchainedFeatures() {
        logger.debug("FM: Hydrating unchained features");
        Map<String, UnchainedDgFeature> mutableUnchainedFeatures = new ConcurrentHashMap<>();
        Stream<List<UnchainedDgFeature>> listStream = getFeatureConfigs().stream()
                .map(featureUnmarshallerService::unmarshall);
        listStream.flatMap(Collection::stream).forEach(f -> {
            UnchainedDgFeature existingFeature = mutableUnchainedFeatures.get(f.getName());
            if (!Objects.isNull(existingFeature)) {
                throw new RuntimeException(String.format("Feature %s from resource %s cannot be loaded. Another "
                                + "feature with same name was previously loaded from resource %s", f.getName(),
                        f.getResourceLocation(), existingFeature.getResourceLocation()));
            }
            mutableUnchainedFeatures.put(f.getName(), f);
        });
        unchainedFeatures = ImmutableMap.copyOf(mutableUnchainedFeatures);
        logger.debug("FM: Hydrated unchained features");
    }

    @Override
    public Set<FeatureConfig> getFeatureConfigs() {
        if (featureConfigs.isEmpty()) {
            for (String resourceLocation : fmProperties.getResources()) {
                try {
                    String content = IOUtils.resourceToString(resourceLocation, StandardCharsets.UTF_8,
                            getClass().getClassLoader());
                    featureConfigs.add(new FeatureConfig(resourceLocation, content));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read feature file: " + resourceLocation, e);
                }
            }
        }
        return unmodifiableFeatureConfigs;
    }

    @Override
    public void addOrReplaceFeatureConfig(FeatureConfig featureConfig) {
        if (fmProperties.isAllowReconfiguration()) {
            FeatureConfig oldConfig = featureConfigs.stream()
                    .filter(c -> c.compareTo(featureConfig) == 0)
                    .findFirst()
                    .orElse(null);

            if (oldConfig != null) {
                featureConfigs.remove(oldConfig);
            }
            featureConfigs.add(featureConfig);

            try {
                init();
            } catch (RuntimeException e) {
                featureConfigs.remove(featureConfig);
                if (oldConfig != null) {
                    featureConfigs.add(oldConfig);
                }

                throw e;
            }

            cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());

            applicationEventPublisher.publishEvent(new FmReconfiguredEvent(this));
        }
    }

    private void chainReferences(Map<String, DgFeature> features,
                                 String fName, Supplier<Set<String>> referenceSupplier,
                                 Consumer<DgFeature> referenceConsumer, String refName) {
        logger.debug(String.format("FM: Chaining references for field %s and reference %s", fName, refName));
        referenceSupplier.get().stream().map(r -> {
            UnchainedDgFeature dgFeature = unchainedFeatures.get(r);
            if (Objects.isNull(dgFeature)) {
                throw new RuntimeException(String.format("Unknown %s %s in feature %s", refName, r, fName));
            }
            return dgFeature;
        }).map(f -> chainFeature(features, f)).forEach(referenceConsumer);
        logger.debug(String.format("FM: Chained references for field %s and reference %s", fName, refName));
    }

    private void chainFeatures(Map<String, DgFeature> features) {
        logger.debug("FM: Chaining features");
        unchainedFeatures.values().forEach(f -> chainFeature(features, f));
        logger.debug("FM: Chained features");
    }

    private DgFeature chainFeature(Map<String, DgFeature> features, UnchainedDgFeature unchained) {
        logger.debug(String.format("FM: Chaining feature %s", unchained));
        if (features.containsKey(unchained.getName())) {
            return features.get(unchained.getName());
        }
        DgFeature chained = new DgFeature();
        try {
            BeanUtils.copyProperties(chained, unchained);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        chainReferences(features, chained);
        features.put(chained.getName(), chained);
        logger.debug(String.format("FM: Chained feature %s", unchained));
        return chained;
    }

    private void chainReferences(Map<String, DgFeature> features, DgFeature f) {
        logger.debug(String.format("FM: Chaining references for feature %s", f));
        chainReferences(features, f.getName(), f::getVisibleDeps, f.getChainedVisibleDeps()::add, "visible deps");
        chainReferences(features, f.getName(), f::getEnabledDeps, f.getChainedEnabledDeps()::add, "enabled deps");
        chainReferences(features, f.getName(), f::getMandatoryDeps, f.getChainedMandatoryDeps()::add, "mandatory deps");
        chainReferences(features, f.getName(), f::getSoftDeps, f.getChainedSoftDeps()::add, "soft deps");
        chainReferences(features, f.getName(), f::getMixins, f.getChainedMixins()::add, "mixins");
        logger.debug(String.format("FM: Chained references for feature %s", f));
    }

}
