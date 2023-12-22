package org.devgateway.ocds.web.rest.controller.flags.crosstab;

import io.swagger.v3.oas.annotations.Operation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.flags.FlagsConstants;
import org.devgateway.ocds.web.rest.controller.flags.AbstractSingleFlagCrosstabController;
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
 * Created by mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagI083CrosstabController extends AbstractSingleFlagCrosstabController {

    @Override
    @Operation(summary = "Crosstab for flag i083")
    @RequestMapping(value = "/api/flags/i083/crosstab",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> flagStats(@ModelAttribute @Valid YearFilterPagingRequest filter) {
        return super.flagStats(FlagsConstants.I083_VALUE, filter);
    }
}
