package org.devgateway.ocds.web.db;

import com.google.common.io.ByteSource;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.devgateway.ocds.persistence.mongo.repository.main.ProcurementPlanMongoRepository;
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
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
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
 *
 * Imports all the makueni data into the mongo db.
 */
@Service
public class ImportPostgresToMongo {
    private static final Logger logger = LoggerFactory.getLogger(ImportPostgresToMongo.class);

    private static final String DOWNLOAD_PREFIX = "/makueniFile/";

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

    @Transactional(readOnly = true)
    public void importToMongo() {
        long startTime = System.nanoTime();
        logger.info("Mongo import started");

        // first clean the procurement plan collection and delete all saved files
        mongoTemplate.dropCollection(ProcurementPlan.class);
        gridFsOperations.delete(new Query(Criteria.where("metadata.path").is(DOWNLOAD_PREFIX)));

        final List<ProcurementPlan> procurementPlans = filterNotExportable(procurementPlanService.findAll());
        // check which forms are exportable
        procurementPlans.parallelStream().forEach(pp -> {
            pp.setProjects(new HashSet<>(filterNotExportable(pp.getProjects())));

            pp.getProjects().parallelStream().forEach(project -> {
                project.setPurchaseRequisitions(
                        new HashSet<>(filterNotExportable(project.getPurchaseRequisitions())));

                project.getPurchaseRequisitions().parallelStream().forEach(pr -> {
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
        gridFsOperations.find(new Query(Criteria.where("metadata.path").is(DOWNLOAD_PREFIX))).into(gridFSFiles);

        procurementPlanMongoRepository.saveAll(procurementPlans);

        // TODO - create more indexes
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("status", Sort.Direction.ASC));
        mongoTemplate.indexOps(ProcurementPlan.class).ensureIndex(
                new Index().on("department.label", Sort.Direction.ASC));


        // clear cache
        cacheManager.getCacheNames().forEach(c -> cacheManager.getCache(c).clear());


        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1000000000.0;
        logger.info("Mongo import ended in: " + duration);
    }

    public void storeMakueniFormFiles(final Set<FileMetadata> formDocs) {
        for (final FileMetadata fileMetadata : formDocs) {
            self.storeFile(fileMetadata);
        }
    }

    public FileMetadata storeFile(final FileMetadata fileMetadata) {
        try {
            if (ObjectUtils.isEmpty(fileMetadata) || ObjectUtils.isEmpty(fileMetadata.getContent())) {
                return null;
            }

            final DBObject metaData = new BasicDBObject();
            metaData.put("path", DOWNLOAD_PREFIX);

            final InputStream is = ByteSource.wrap(fileMetadata.getContent().getBytes()).openStream();
            final ObjectId objId = gridFsOperations.store(
                    is, fileMetadata.getName(), fileMetadata.getContentType(), metaData);
            is.close();

            fileMetadata.setUrl(DOWNLOAD_PREFIX + objId);

            return fileMetadata;
        } catch (IOException e) {
            logger.error("Error wile saving a file.", e);
            throw new RuntimeException(e);
        }
    }

    public GridFSFile retrieveFile(ObjectId id) {
        return gridFsOperations.findOne(new Query(Criteria.where(Fields.UNDERSCORE_ID).is(id)));
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
