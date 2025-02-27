package org.devgateway.ocds.web.rest.controller.flags;

import io.swagger.v3.oas.annotations.Operation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Created by mpostelnicu on 12/2/2016.
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagI182ReleaseSearchController extends AbstractSingleFlagReleaseSearchController {

    @Override
    @Operation(summary = "Search releases by flag i182")
    @RequestMapping(value = "/api/flags/i182/releases",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> releaseFlagSearch(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagSearch(FlagsConstants.I182_VALUE, filter);
    }

    @Override
    @Operation(summary = "Counts releases by flag i182")
    @RequestMapping(value = "/api/flags/i182/count",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> releaseFlagCount(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagCount(FlagsConstants.I182_VALUE, filter);
    }
}
