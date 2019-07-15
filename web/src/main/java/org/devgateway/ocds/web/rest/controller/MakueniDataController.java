package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.MakueniFilterPagingRequest;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

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

    @ApiOperation(value = "Fetch Makueni Procurement Plans")
    @RequestMapping(value = "/api/makueni/procurementPlans",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<ProcurementPlan> makueniProcurementPlans(
            @ModelAttribute @Valid final MakueniFilterPagingRequest filter) {
        final AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).build();

        final Criteria criteria = new Criteria().andOperator(
                createFilterCriteria("department._id", filter.getDepartment()),
                createFilterCriteria("fiscalYear._id", filter.getFiscalYear()));

        final Aggregation aggregation = newAggregation(match(criteria),
                project("formDocs", "department", "fiscalYear", "status", "approvedDate"));

        return mongoTemplate.aggregate(aggregation.withOptions(options), "procurementPlan", ProcurementPlan.class)
                .getMappedResults();
    }

    @ApiOperation(value = "Counts Makueni Procurement Plans")
    @RequestMapping(value = "/api/makueni/procurementPlansCount",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public Integer makueniProcurementPlansCount(@ModelAttribute @Valid final MakueniFilterPagingRequest filter) {
        return makueniProcurementPlans(filter).size();
    }

    @ApiOperation(value = "Display the available Procurement Plan Departments.")
    @RequestMapping(value = "/api/makueni/filters/departments", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> getDepartments() {
        final AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).build();

        final DBObject project = new BasicDBObject("_id._id", 1);
        project.put("_id.label", 1);
        project.put("_id.code", 1);

        final Aggregation aggregation = newAggregation(project("department", Fields.UNDERSCORE_ID),
                group("department"), new CustomOperation(new Document("$project", project)));

        return mongoTemplate.aggregate(aggregation.withOptions(options), "procurementPlan", Document.class)
                .getMappedResults();
    }

    @ApiOperation(value = "Display the available Procurement Plan FY.")
    @RequestMapping(value = "/api/makueni/filters/fiscalYears", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> getFiscalYears() {
        final AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).build();

        final DBObject project = new BasicDBObject("_id._id", 1);
        project.put("_id.label", "$_id.name");
        project.put("_id.startDate", 1);
        project.put("_id.endDate", 1);

        final Aggregation aggregation = newAggregation(project("fiscalYear", Fields.UNDERSCORE_ID),
                group("fiscalYear"), new CustomOperation(new Document("$project", project)));

        return mongoTemplate.aggregate(aggregation.withOptions(options), "procurementPlan", Document.class)
                .getMappedResults();
    }

    private Criteria createFilterCriteria(final String filterName, final Object filterValues) {
        if (filterValues == null) {
            return new Criteria();
        }

        return where(filterName).is(filterValues);
    }
}
