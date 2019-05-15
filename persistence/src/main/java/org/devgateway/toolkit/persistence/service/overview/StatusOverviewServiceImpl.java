package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.ProjectAttachable;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;
import org.devgateway.toolkit.persistence.dto.ProjectStatus;
import org.devgateway.toolkit.persistence.repository.form.AwardAcceptanceRepository;
import org.devgateway.toolkit.persistence.repository.form.AwardNotificationRepository;
import org.devgateway.toolkit.persistence.repository.form.ContractRepository;
import org.devgateway.toolkit.persistence.repository.form.ProfessionalOpinionRepository;
import org.devgateway.toolkit.persistence.repository.form.ProjectRepository;
import org.devgateway.toolkit.persistence.repository.form.PurchaseRequisitionRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderQuotationEvaluationRepository;
import org.devgateway.toolkit.persistence.repository.form.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatusOverviewServiceImpl implements StatusOverviewService {
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
    @Override
    public List<DepartmentOverviewData> getProjectsByDepartment(final Long fiscaYearId) {
        List<DepartmentOverviewData> departmentsData = new ArrayList<>();
        List<Project> projects = projectRepository.findProjectsForYear(fiscaYearId);
        final Map<Long, Set<String>> tenderStatusMap = getTenderStatusMap(null);
        final Map<Long, Set<String>> awardStatusMap = getAwardStatusMap(null);

        for (Project p : projects) {
            DepartmentOverviewData departmentOverview = departmentsData.stream().filter(d -> {
                return p.getProcurementPlan().equals(d.getProcurementPlan());
            }).findFirst().orElse(null);

            if (departmentOverview == null) {
                departmentOverview = new DepartmentOverviewData();
                departmentOverview.setProcurementPlan(p.getProcurementPlan());
                departmentsData.add(departmentOverview);
            }

            ProjectStatus projectStatus = new ProjectStatus();
            projectStatus.setId(p.getId());
            projectStatus.setProjectTitle(p.getProjectTitle());
            projectStatus.setProjectStatus(p.getStatus());
            projectStatus.setTenderStatus(getProcessStatus(tenderStatusMap, p.getId()));
            projectStatus.setAwardStatus(getProcessStatus(awardStatusMap, p.getId()));
            departmentOverview.getProjects().add(projectStatus);

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
        addRequisitionStatus(statusMap, fiscalYearId);
        addTenderStatus(statusMap, fiscalYearId);
        addTenderQuotationStatus(statusMap, fiscalYearId);
        addProfessionalOpinionStatus(statusMap, fiscalYearId);
        return statusMap;
    }

    private Map<Long, Set<String>> getAwardStatusMap(final Long fiscalYearId) {
        final Map<Long, Set<String>> statusMap = new HashMap<>();
        addAwardNotificationStatus(statusMap, fiscalYearId);
        addAwardAcceptanceStatus(statusMap, fiscalYearId);
        addContractStatus(statusMap, fiscalYearId);
        return statusMap;
    }

    private void addRequisitionStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        // TODO: filter for current years requisitions
        final List<PurchaseRequisition> purchaseRequisitionList = purchaseRequisitionRepository.findAll();
        addStatus(statusMap, fiscalYearId, purchaseRequisitionList);
    }

    private <S extends ProjectAttachable & Statusable>
            void addStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId,
                           final Collection<S> collection) {
            collection.stream().filter(ProjectAttachable::hasProjectId)
                    .forEach(e -> addStatus(statusMap, e.getProjectId(), e.getStatus()));
    }

    private void addTenderStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        // TODO: replace with filtered Tender List
        final List<Tender> tenderList = tenderRepository.findAll();
        addStatus(statusMap, fiscalYearId, tenderList);
    }

    private void addTenderQuotationStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        final List<TenderQuotationEvaluation> tenderEvaluationsList = tenderQuotationEvaluationRepository.findAll();
        addStatus(statusMap, fiscalYearId, tenderEvaluationsList);
    }

    private void addProfessionalOpinionStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        final List<ProfessionalOpinion> professionalOpinionList = professionalOpinionRepository.findAll();
        addStatus(statusMap, fiscalYearId, professionalOpinionList);
    }

    private void addAwardNotificationStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        List<AwardNotification> awardNotificationList = awardNotificationRepository.findAll();
        addStatus(statusMap, fiscalYearId, awardNotificationList);
    }

    private void addAwardAcceptanceStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        List<AwardAcceptance> awardAcceptanceList = awardAcceptanceRepository.findAll();
        addStatus(statusMap, fiscalYearId, awardAcceptanceList);
    }

    private void addContractStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        List<Contract> contractList = contractRepository.findAll();
        addStatus(statusMap, fiscalYearId, contractList);
    }

    private void addStatus(final Map<Long, Set<String>> statusMap, final Long projectId, final String status) {
        Set<String> statuses = statusMap.computeIfAbsent(projectId, k -> new HashSet<>());
        statuses.add(status);
    }

}
