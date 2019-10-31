package org.devgateway.ocds.web.db;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.lang3.time.StopWatch;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.persistence.mongo.repository.main.ProcurementPlanMongoRepository;
import org.devgateway.ocds.web.convert.MongoFileStorageService;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
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
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-07-02
 * <p>
 * <p>
 * Imports all the makueni data into the mongo db.
 */
@Service
public class ImportPostgresToMongo {
    private static final Logger logger = LoggerFactory.getLogger(ImportPostgresToMongo.class);

    @Resource
    private ImportPostgresToMongo self; // Self-autowired reference to proxified bean of this class.

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProcurementPlanMongoRepository procurementPlanMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Autowired
    private MongoFileStorageService mongoFileStorageService;


    @Transactional(readOnly = true)
    public void importToMongo() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("Mongo import started");

        // first clean the procurement plan collection and delete all saved files
        mongoTemplate.dropCollection(ProcurementPlan.class);
        gridFsOperations.delete(MongoFileStorageService.MAKUENI_FILES_QUERY);

        final List<ProcurementPlan> procurementPlans = filterNotExportable(procurementPlanService.findAll());
        // check which forms are exportable
        procurementPlans.stream().forEach(pp -> {
            pp.setProjects(new HashSet<>(filterNotExportable(pp.getProjects())));

            pp.getProjects().stream().forEach(project -> {
                project.setTenderProcesses(
                        new HashSet<>(filterNotExportable(project.getTenderProcesses())));

                project.getTenderProcesses().stream().forEach(pr -> {
                    pr.setTender(new HashSet<>(filterNotExportable(pr.getTender())));
                    pr.getTender().stream().forEach(item -> self.storeMakueniFormFiles(item.getFormDocs()));

                    pr.setTenderQuotationEvaluation(new HashSet<>(
                            filterNotExportable(pr.getTenderQuotationEvaluation())));
                    pr.getTenderQuotationEvaluation().stream()
                            .forEach(item -> self.storeMakueniFormFiles(item.getFormDocs()));

                    pr.setProfessionalOpinion(new HashSet<>(filterNotExportable(pr.getProfessionalOpinion())));
                    pr.getProfessionalOpinion().stream()
                            .forEach(item -> self.storeMakueniFormFiles(item.getFormDocs()));

                    pr.setAwardNotification(new HashSet<>(filterNotExportable(pr.getAwardNotification())));
                    pr.getAwardNotification().stream()
                            .forEach(item -> self.storeMakueniFormFiles(item.getFormDocs()));

                    pr.setAwardAcceptance(new HashSet<>(filterNotExportable(pr.getAwardAcceptance())));
                    pr.getAwardAcceptance().stream()
                            .forEach(item -> self.storeMakueniFormFiles(item.getFormDocs()));

                    pr.setContract(new HashSet<>(filterNotExportable(pr.getContract())));
                    pr.getContract().stream()
                            .forEach(item -> item.getContractDocs().stream()
                                    .forEach(doc -> self.storeMakueniFormFiles(doc.getFormDocs())));

                    self.storeMakueniFormFiles(pr.getFormDocs());
                });

                self.storeMakueniFormFiles(project.getCabinetPaper().getFormDocs());
            });

            self.storeMakueniFormFiles(pp.getFormDocs());
        });


        final List<GridFSFile> gridFSFiles = new ArrayList<>();
        gridFsOperations.find(MongoFileStorageService.MAKUENI_FILES_QUERY).into(gridFSFiles);

        procurementPlanMongoRepository.saveAll(procurementPlans);

        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("status", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("department._id", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("fiscalYear._id", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.subcounties._id", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.wards._id", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.tenderProcesses.tender.tenderItems.purchaseItem.planItem.item._id",
                        Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.tenderProcesses.tender.tenderValue", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.tenderProcesses.tender.closingDate", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.tenderProcesses.lastModifiedDate", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("projects.tenderProcesses.tender.lastModifiedDate", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new TextIndexDefinition.TextIndexDefinitionBuilder()
                        .withDefaultLanguage(MongoConstants.MONGO_LANGUAGE)
                        .onField("projects.tenderProcesses.tender.tenderTitle")
                        .onField("projects.projectTitle")
                        .build());

        // clear cache
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());

        stopWatch.stop();
        logger.info("Mongo import ended in: " + stopWatch.getTime() + "ms");
    }

    public void storeMakueniFormFiles(final Set<FileMetadata> formDocs) {
        for (final FileMetadata fileMetadata : formDocs) {
            mongoFileStorageService.storeFile(fileMetadata);
        }
    }

    /**
     * Filter a {@link Statusable} collection and keep only the exportable items.
     *
     * @param collection
     * @param <S>
     * @return
     */
    private <S extends Statusable> List<S> filterNotExportable(final Collection<S> collection) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }

        return collection.stream()
                .filter(item -> item.isExportable())
                .collect(Collectors.toList());
    }
}
