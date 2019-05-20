package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.ProjectAttachable;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
import org.devgateway.toolkit.persistence.dto.StatusOverviewProjectStatus;
import org.devgateway.toolkit.persistence.repository.form.AbstractMakueniEntityRepository;
import org.devgateway.toolkit.persistence.repository.form.AwardAcceptanceRepository;
import org.devgateway.toolkit.persistence.repository.form.AwardNotificationRepository;
import org.devgateway.toolkit.persistence.repository.form.ContractRepository;
import org.devgateway.toolkit.persistence.repository.form.ProfessionalOpinionRepository;
import org.devgateway.toolkit.persistence.repository.form.PurchaseRequisitionRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderQuotationEvaluationRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderRepository;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProjectFilterState;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gmutuhu
 */
@Service
@CacheConfig(keyGenerator = "genericKeyGenerator", cacheNames = "servicesCache")
// TODO - add cache
public class StatusOverviewServiceImpl implements StatusOverviewService {
    // TODO - replace all repositories with services
    @Autowired
    private ProjectService projectService;

    @Autowired
    private PurchaseRequisitionRepository purchaseRequisitionRepository;

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private TenderQuotationEvaluationRepository tenderQuotationEvaluationRepository;

    @Autowired
    private ProfessionalOpinionRepository professionalOpinionRepository;

    @Autowired
    private AwardNotificationRepository awardNotificationRepository;

    @Autowired
    private AwardAcceptanceRepository awardAcceptanceRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public List<StatusOverviewData> getAllProjects(final FiscalYear fiscalYear, final String projectTitle) {
        final List<StatusOverviewData> statusOverviewData = new ArrayList<>();
        final List<Project> projects = projectService.findAll(
                new ProjectFilterState(fiscalYear, projectTitle).getSpecification());

        final Map<Long, Set<String>> tenderStatusMap = addStatus(
                fiscalYear, purchaseRequisitionRepository, tenderRepository,
                tenderQuotationEvaluationRepository, professionalOpinionRepository);
        final Map<Long, Set<String>> awardStatusMap = addStatus(
                fiscalYear, awardNotificationRepository, awardAcceptanceRepository, contractRepository);

        for (final Project p : projects) {
            StatusOverviewData sod = statusOverviewData.parallelStream()
                    .filter(item -> p.getProcurementPlan().equals(item.getProcurementPlan()))
                    .findFirst()
                    .orElse(null);
            if (sod == null) {
                sod = new StatusOverviewData();
                sod.setProcurementPlan(p.getProcurementPlan());
                statusOverviewData.add(sod);
            }

            final StatusOverviewProjectStatus statusOverviewProjectStatus = new StatusOverviewProjectStatus();
            statusOverviewProjectStatus.setId(p.getId());
            statusOverviewProjectStatus.setProjectTitle(p.getProjectTitle());
            statusOverviewProjectStatus.setProjectStatus(p.getStatus());
            statusOverviewProjectStatus.setTenderProcessStatus(getProcessStatus(tenderStatusMap, p.getId()));
            statusOverviewProjectStatus.setAwardProcessStatus(getProcessStatus(awardStatusMap, p.getId()));

            sod.getProjects().add(statusOverviewProjectStatus);
        }

        return statusOverviewData;
    }

    private String getProcessStatus(final Map<Long, Set<String>> tenderStatusMap, final Long projectId) {
        final Set<String> status = tenderStatusMap.get(projectId);

        if (status != null) {
            if (status.contains(DBConstants.Status.TERMINATED)) {
                return DBConstants.Status.TERMINATED;
            }

            if (status.contains(DBConstants.Status.DRAFT)) {
                return DBConstants.Status.DRAFT;
            }

            if (status.contains(DBConstants.Status.SUBMITTED)) {
                return DBConstants.Status.SUBMITTED;
            }

            if (status.contains(DBConstants.Status.APPROVED)) {
                return DBConstants.Status.APPROVED;
            }
        }

        return DBConstants.Status.NOT_STARTED;
    }

    @SafeVarargs
    private final <S extends AbstractMakueniEntity & ProjectAttachable & Statusable>
    Map<Long, Set<String>> addStatus(final FiscalYear fiscalYear,
                                     final AbstractMakueniEntityRepository<? extends S>... repositories) {
        final Map<Long, Set<String>> statusMap = new HashMap<>();

        for (AbstractMakueniEntityRepository<? extends S> repository : repositories) {
            addStatus(statusMap, repository.findByFiscalYear(fiscalYear));
        }

        return statusMap;
    }

    private <S extends ProjectAttachable & Statusable>
    void addStatus(final Map<Long, Set<String>> statusMap,
                   final Collection<S> collection) {
        collection.stream()
                .forEach(e -> addStatus(statusMap, e.getProject().getId(), e.getStatus()));
    }

    private void addStatus(final Map<Long, Set<String>> statusMap, final Long projectId, final String status) {
        Set<String> statuses = statusMap.computeIfAbsent(projectId, k -> new HashSet<>());
        statuses.add(status);
    }
}
