package org.devgateway.ocds.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.ocds.web.rest.controller.request.MakueniFilterPagingRequest;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author idobre
 * @since 2019-07-12
 */

@RestController
// @CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
// @Cacheable
public class MakueniDataController extends GenericOCDSController {

    @ApiOperation(value = "Fetch Makueni Tenders")
    @RequestMapping(value = "/api/makueni/tenders",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<ProcurementPlan> makueniTenders(@ModelAttribute @Valid final MakueniFilterPagingRequest filter) {

        return mongoTemplate.findAll(ProcurementPlan.class);
    }

    @ApiOperation(value = "Counts Makueni Tenders")
    @RequestMapping(value = "/api/makueni/tendersCount",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public Long makueniTendersCount(@ModelAttribute @Valid final MakueniFilterPagingRequest filter) {

        return mongoTemplate.count(new Query(), ProcurementPlan.class);
    }
}
