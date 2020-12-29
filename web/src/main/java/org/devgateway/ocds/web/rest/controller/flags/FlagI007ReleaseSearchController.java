package org.devgateway.ocds.web.rest.controller.flags;

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mpostelnicu on 12/2/2016.
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagI007ReleaseSearchController extends AbstractSingleFlagReleaseSearchController {

    @Override
    @ApiOperation(value = "Search releases by flag i007")
    @RequestMapping(value = "/api/flags/i007/releases",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public List<Document> releaseFlagSearch(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagSearch(FlagsConstants.I007_VALUE, filter);
    }

    @Override
    @ApiOperation(value = "Counts releases by flag i007")
    @RequestMapping(value = "/api/flags/i007/count",
            method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public List<Document> releaseFlagCount(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.releaseFlagCount(FlagsConstants.I007_VALUE, filter);
    }
}
