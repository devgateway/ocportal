package org.devgateway.ocds.web.rest.controller.flags;

import io.swagger.v3.oas.annotations.Operation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.web.rest.controller.request.FlagsWithFilterRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author Octavian Ciubotaru
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagsReleaseSearchController extends AbstractFlagReleaseSearchController {

    @Operation(summary = "Search releases by flags")
    @RequestMapping(value = "/api/flags/releases",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public Map<String, List<Document>> releaseFlagSearch(@ModelAttribute @Valid FlagsWithFilterRequest filter) {
        return filter.getFlags().stream().collect(toMap(
                identity(),
                flag -> releaseFlagSearch(FlagsConstants.FLAG_VALUES_BY_NAME.get(flag), filter)));
    }

    @Operation(summary = "Counts releases by flags")
    @RequestMapping(value = "/api/flags/count",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public Map<String, List<Document>> releaseFlagCount(@ModelAttribute @Valid FlagsWithFilterRequest filter) {
        return filter.getFlags().stream().collect(toMap(
                identity(),
                flag -> releaseFlagCount(FlagsConstants.FLAG_VALUES_BY_NAME.get(flag), filter)));
    }
}
