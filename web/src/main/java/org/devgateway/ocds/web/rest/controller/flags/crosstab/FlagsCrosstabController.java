package org.devgateway.ocds.web.rest.controller.flags.crosstab;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devgateway.ocds.persistence.mongo.flags.FlagsConstants.FLAG_VALUES_BY_NAME;

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.flags.AbstractFlagCrosstabController;
import org.devgateway.ocds.web.rest.controller.request.FlagsWithFilterRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FlagsCrosstabController extends AbstractFlagCrosstabController {

    @ApiOperation(value = "Crosstab for flags")
    @RequestMapping(value = "/api/flags/crosstab",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public Map<String, List<Document>> flagStats(@ModelAttribute @Valid FlagsWithFilterRequest request) {
        return request.getFlags().stream().collect(toMap(
                identity(),
                name -> flagStats(FLAG_VALUES_BY_NAME.get(name), request)));
    }
}
