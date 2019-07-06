package org.devgateway.ocds.web.db;

import org.devgateway.ocds.persistence.mongo.repository.main.ProcurementPlanMongoRepository;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-07-02
 * <p>
 * TODO
 */
@Service
public class ImportPostgresToMongo {
    protected static final Logger logger = LoggerFactory.getLogger(ImportPostgresToMongo.class);

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProcurementPlanMongoRepository procurementPlanMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Transactional(readOnly = true)
    public void test() {
        long startTime = System.nanoTime();

        final List<ProcurementPlan> procurementPlans = filterNotExportable(procurementPlanService.findAll());
        // check which forms are exportable
        procurementPlans.parallelStream().forEach(pp -> {
            pp.setProjects(new HashSet<>(filterNotExportable(pp.getProjects())));

            pp.getProjects().parallelStream().forEach(project -> {
                project.setPurchaseRequisitions(
                        new HashSet<>(filterNotExportable(project.getPurchaseRequisitions())));
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setTender(new HashSet<>(filterNotExportable(pr.getTender())));
                });
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setTenderQuotationEvaluation(new HashSet<>(
                            filterNotExportable(pr.getTenderQuotationEvaluation())));
                });
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setProfessionalOpinion(new HashSet<>(filterNotExportable(pr.getProfessionalOpinion())));
                });
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setAwardNotification(new HashSet<>(filterNotExportable(pr.getAwardNotification())));
                });
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setAwardAcceptance(new HashSet<>(filterNotExportable(pr.getAwardAcceptance())));
                });
                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
                    pr.setContract(new HashSet<>(filterNotExportable(pr.getContract())));
                });
            });
        });


        // first clean the procurement plan collection and then save everything
        mongoTemplate.dropCollection(ProcurementPlan.class);
        procurementPlanMongoRepository.saveAll(procurementPlans);

        // TODO - create more indexes
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("status", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("department.label", Sort.Direction.ASC));


        // clear cache
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());


        long endTime = System.nanoTime();
        logger.info("-------------------------------------------------------------------------------");
        double duration = (endTime - startTime) / 1000000000.0;
        logger.info("------- PROCESSING TIME: " + duration);
    }

    /**
     * Filter a {@link Statusable} collection and keep only the exportable items.
     *
     * @param collection
     * @param <S>
     * @return
     */
    private <S extends Statusable> List<S> filterNotExportable(final Collection<S> collection) {
        return collection.parallelStream()
                .filter(item -> item.isExportable())
                .collect(Collectors.toList());
    }
}
