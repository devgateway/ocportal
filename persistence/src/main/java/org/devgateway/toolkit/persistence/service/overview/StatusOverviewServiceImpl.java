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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final List<Project> projects = projectService.findAll(
                new ProjectFilterState(fiscalYear, projectTitle).getSpecification());

        final Map<Project, List<String>> tenderStatusMap = addStatus(
                fiscalYear, purchaseRequisitionRepository, tenderRepository,
                tenderQuotationEvaluationRepository, professionalOpinionRepository);
        final Map<Project, List<String>> awardStatusMap = addStatus(
                fiscalYear, awardNotificationRepository, awardAcceptanceRepository, contractRepository);

        // get list of statuses of PurchaseRequisition forms grouped by Project
        final Map<Project, List<String>> purchaseStatusMap = groupStatusByProject(
                purchaseRequisitionRepository.findByFiscalYear(fiscalYear));

        final List<StatusOverviewData> statusOverviewData = new ArrayList<>();
        for (final Project project : projects) {
            StatusOverviewData sod = statusOverviewData.parallelStream()
                    .filter(item -> project.getProcurementPlan().equals(item.getProcurementPlan()))
                    .findFirst()
                    .orElse(null);
            if (sod == null) {
                sod = new StatusOverviewData();
                sod.setProcurementPlan(project.getProcurementPlan());
                statusOverviewData.add(sod);
            }

            final List<String> purchaseStatuses = purchaseStatusMap.get(project);

            final StatusOverviewProjectStatus statusOverviewProjectStatus = new StatusOverviewProjectStatus();
            statusOverviewProjectStatus.setId(project.getId());
            statusOverviewProjectStatus.setProjectTitle(project.getProjectTitle());
            statusOverviewProjectStatus.setProjectStatus(project.getStatus());

            // check if we have at least one Purchase Requisition
            if (purchaseStatuses == null || purchaseStatuses.isEmpty()) {
                statusOverviewProjectStatus.setTenderProcessStatus(DBConstants.Status.NOT_STARTED);
                statusOverviewProjectStatus.setAwardProcessStatus(DBConstants.Status.NOT_STARTED);
            } else {
                statusOverviewProjectStatus.setTenderProcessStatus(
                        getProcessStatus(tenderStatusMap.get(project), purchaseStatuses.size() * 4));
                statusOverviewProjectStatus.setAwardProcessStatus(
                        getProcessStatus(awardStatusMap.get(project), purchaseStatuses.size() * 3));
            }

            sod.getProjects().add(statusOverviewProjectStatus);
        }

        return statusOverviewData;
    }

    /**
     * Returns an aggregate status from a list of statuses.
     *
     * @param sizeCheck - is used to determine if the list of statuses has the right length
     */
    private String getProcessStatus(final List<String> statuses, final int sizeCheck) {
        if (statuses != null) {
            if (statuses.contains(DBConstants.Status.TERMINATED)) {
                return DBConstants.Status.TERMINATED;
            }

            if (statuses.size() != sizeCheck) {
                return DBConstants.Status.NOT_STARTED;
            }

            if (statuses.contains(DBConstants.Status.DRAFT)) {
                return DBConstants.Status.DRAFT;
            }

            if (statuses.contains(DBConstants.Status.SUBMITTED)) {
                return DBConstants.Status.SUBMITTED;
            }

            if (statuses.contains(DBConstants.Status.APPROVED)) {
                return DBConstants.Status.APPROVED;
            }
        }

        return DBConstants.Status.NOT_STARTED;
    }

    /**
     * Group a list of {@link ProjectAttachable} objects by {@link Project} and collect all their status.
     */
    private <S extends ProjectAttachable & Statusable>
    Map<Project, List<String>> groupStatusByProject(final List<S> list) {
        return list.stream()
                .collect(Collectors.groupingBy(ProjectAttachable::getProject,
                        Collectors.mapping(key -> key.getStatus(), Collectors.toList())));
    }

    /**
     * Merge 2 {@link Map} of statuses grouped by {@link Project}.
     */
    private Map<Project, List<String>> mergeMapOfStatuses(final Map<Project, List<String>> map1,
                                                          final Map<Project, List<String>> map2) {
        final Map<Project, List<String>> mergedMap = new HashMap<>(map1);
        map2.forEach(
                (key, value) -> mergedMap.merge(key, value,
                        (v1, v2) -> Stream.of(v1, v2).flatMap(Collection::stream).collect(Collectors.toList())));

        return mergedMap;
    }

    private <S extends AbstractMakueniEntity & ProjectAttachable & Statusable>
    Map<Project, List<String>> addStatus(final FiscalYear fiscalYear,
                                         final AbstractMakueniEntityRepository<? extends S>... repositories) {
        Map<Project, List<String>> statusMap = new HashMap<>();

        for (AbstractMakueniEntityRepository<? extends S> repository : repositories) {
            statusMap = mergeMapOfStatuses(statusMap, groupStatusByProject(repository.findByFiscalYear(fiscalYear)));
        }

        return statusMap;
    }
}
