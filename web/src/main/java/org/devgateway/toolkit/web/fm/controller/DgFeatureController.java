package org.devgateway.toolkit.web.fm.controller;

import org.devgateway.toolkit.web.fm.entity.DgFeature;
import org.devgateway.toolkit.web.fm.request.FmRequestParam;
import org.devgateway.toolkit.web.fm.service.DgFmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 */
@RestController
@Validated
@CacheConfig(cacheNames = "fmCache")
public class DgFeatureController {

    @Autowired
    private DgFmService fmService;

    @PostMapping(value = "/api/fm/featureProperties", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Validated
    @Cacheable(key = "#fmParameters.fmNames.toString() + #fmParameters.fmPrefixes.toString()")
    public List<DgFeature> featureProperties(@Valid @ModelAttribute FmRequestParam fmParameters) {
        if (!ObjectUtils.isEmpty(fmParameters.getFmNames())) {
            return fmParameters.getFmNames().stream().map(fmService::getFeature).collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(fmParameters.getFmPrefixes())) {
            return fmParameters.getFmPrefixes().stream().map(fmService::getFeaturesByPrefix).flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
        }
        throw new RuntimeException("Invalid request");
    }
}
