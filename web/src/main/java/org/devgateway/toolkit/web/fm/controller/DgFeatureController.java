package org.devgateway.toolkit.web.fm.controller;

import org.devgateway.toolkit.web.fm.entity.DgFeature;
import org.devgateway.toolkit.web.fm.service.DgFmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 */
@RestController
@Validated
public class DgFeatureController {

    @Autowired
    private DgFmService fmService;

    public static class FeaturePropertiesParameters implements Serializable {
        @NotEmpty
        private List<String> fmNames;

        public List<String> getFmNames() {
            return fmNames;
        }

        public void setFmNames(List<String> fmNames) {
            this.fmNames = fmNames;
        }
    }

    @RequestMapping(value = "/api/fm/featureProperties",
            method = {RequestMethod.POST, RequestMethod.GET},
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DgFeature> featureProperties(@Valid @RequestBody FeaturePropertiesParameters fmParameters) {
        return fmParameters.getFmNames().stream().map(fmService::getFeature).collect(Collectors.toList());
    }
}
