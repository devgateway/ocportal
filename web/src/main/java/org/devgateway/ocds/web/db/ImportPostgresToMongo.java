package org.devgateway.ocds.web.db;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.devgateway.ocds.persistence.mongo.repository.main.ProcurementPlanMongoRepository;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-07-02
 */
@Service
public class ImportPostgresToMongo {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProcurementPlanMongoRepository procurementPlanMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoCustomConversions mongoCustomConversions;

    @Transactional(readOnly = true)
    public void test() throws JsonProcessingException {
        // TODO - filtrare aici dupa status
        final List<ProcurementPlan> procurementPlans = procurementPlanService.findAll();
        final ProcurementPlan procurementPlan = procurementPlanService.findById(new Long(17257)).get();

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        final String jsonString = mapper.writeValueAsString(procurementPlan);
        System.out.println("jsonString = " + jsonString);

        procurementPlanMongoRepository.save(procurementPlan);

        // mongoTemplate.insert(jsonString, "procurementPlan");
    }
}
