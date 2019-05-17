package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dao.DBConstants;
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
import org.devgateway.toolkit.persistence.repository.form.ProjectRepository;
import org.devgateway.toolkit.persistence.repository.form.PurchaseRequisitionRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderQuotationEvaluationRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderRepository;
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
    private ProjectRepository projectRepository;

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

    // TODO: apply filters, investigate if the tender status and award status can be
    // optimized by using sql queries
    // TODO change the name of the function or update it's parameters
    //  since we don't actually fetch the projects 'byDepartment'
    @Override
    public List<StatusOverviewData> getProjectsByDepartment(final Long fiscalYearId) {
        List<StatusOverviewData> departmentsData = new ArrayList<>();
        List<Project> projects = projectRepository.findProjectsForYear(fiscalYearId);
        final Map<Long, Set<String>> tenderStatusMap = getTenderStatusMap(fiscalYearId);
        final Map<Long, Set<String>> awardStatusMap = getAwardStatusMap(fiscalYearId);

        for (Project p : projects) {
            StatusOverviewData departmentOverview = departmentsData.stream().filter(
                    d -> p.getProcurementPlan().equals(d.getProcurementPlan())).findFirst().orElse(null);
            if (departmentOverview == null) {
                departmentOverview = new StatusOverviewData();
                departmentOverview.setProcurementPlan(p.getProcurementPlan());
                departmentsData.add(departmentOverview);
            }

            StatusOverviewProjectStatus statusOverviewProjectStatus = new StatusOverviewProjectStatus();
            statusOverviewProjectStatus.setId(p.getId());
            statusOverviewProjectStatus.setProjectTitle(p.getProjectTitle());
            statusOverviewProjectStatus.setProjectStatus(p.getStatus());
            statusOverviewProjectStatus.setTenderProcessStatus(getProcessStatus(tenderStatusMap, p.getId()));
            statusOverviewProjectStatus.setAwardProcessStatus(getProcessStatus(awardStatusMap, p.getId()));
            departmentOverview.getProjects().add(statusOverviewProjectStatus);
        }
        return departmentsData;
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

            if (status.contains(DBConstants.Status.VALIDATED)) {
                return DBConstants.Status.VALIDATED;
            }
        }

        return DBConstants.Status.NOT_STARTED;
    }

    private Map<Long, Set<String>> getTenderStatusMap(final Long fiscalYearId) {
        final Map<Long, Set<String>> statusMap = new HashMap<>();
        addStatus(
                statusMap, fiscalYearId, purchaseRequisitionRepository, tenderRepository,
                tenderQuotationEvaluationRepository, professionalOpinionRepository
        );
        return statusMap;
    }

    private Map<Long, Set<String>> getAwardStatusMap(final Long fiscalYearId) {
        final Map<Long, Set<String>> statusMap = new HashMap<>();
        addStatus(
                statusMap, fiscalYearId, awardNotificationRepository, awardAcceptanceRepository, contractRepository);
        return statusMap;
    }

    private <S extends ProjectAttachable & Statusable>
    void addStatus(final Map<Long, Set<String>> statusMap,
                   final Collection<S> collection) {
        collection.stream()
                .forEach(e -> addStatus(statusMap, e.getProject().getId(), e.getStatus()));
    }

    @SafeVarargs
    private final <S extends AbstractMakueniEntity & ProjectAttachable & Statusable>
    void addStatus(final Map<Long, Set<String>> statusMap,
                   final Long fiscalYearId,
                   final AbstractMakueniEntityRepository<? extends S>... repository) {
        for (AbstractMakueniEntityRepository<? extends S> r : repository) {
            addStatus(statusMap, r.findByFiscalYearId(fiscalYearId));
        }
    }

    private void addStatus(final Map<Long, Set<String>> statusMap, final Long projectId, final String status) {
        Set<String> statuses = statusMap.computeIfAbsent(projectId, k -> new HashSet<>());
        statuses.add(status);
    }
}
