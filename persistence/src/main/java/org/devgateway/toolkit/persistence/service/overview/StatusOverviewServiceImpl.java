package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
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
        for (PurchaseRequisition pr : purchaseRequisitionList) {
            if (pr.getProject() != null) {
                Long projectId = pr.getProject() != null ? pr.getProject().getId() : null;
                addStatus(statusMap, projectId, pr.getStatus());
            }

        }
    }

    private void addTenderStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        // TODO: replace with filtered Tender List
        final List<Tender> tenderList = tenderRepository.findAll();
        for (Tender tender : tenderList) {

            if ((tender.getPurchaseRequisition() != null && tender.getPurchaseRequisition().getProject() != null)) {
                Long projectId = tender.getPurchaseRequisition().getProject().getId();
                addStatus(statusMap, projectId, tender.getStatus());
            }
        }
    }

    private void addTenderQuotationStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        final List<TenderQuotationEvaluation> tenderEvaluationsList = tenderQuotationEvaluationRepository.findAll();
        for (TenderQuotationEvaluation evaluation : tenderEvaluationsList) {

            if (evaluation.getTender() != null && evaluation.getTender().getPurchaseRequisition() != null
                    && evaluation.getTender().getPurchaseRequisition().getProject() != null) {
                Long projectId = evaluation.getTender().getPurchaseRequisition().getProject().getId();
                addStatus(statusMap, projectId, evaluation.getStatus());
            }

        }
    }

    private void addProfessionalOpinionStatus(final Map<Long, Set<String>> statusMap, final Long fiscalYearId) {
        final List<ProfessionalOpinion> professionalOpinionList = professionalOpinionRepository.findAll();
        for (ProfessionalOpinion professionalOpinion : professionalOpinionList) {

            if (professionalOpinion.getTenderQuotationEvaluation() != null
                    && professionalOpinion.getTenderQuotationEvaluation().getTender() != null
                    && professionalOpinion.getTenderQuotationEvaluation().getTender().getPurchaseRequisition() != null
                    && professionalOpinion.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                    .getProject() != null) {

                Long projectId = professionalOpinion.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                        .getProject().getId();
                addStatus(statusMap, projectId, professionalOpinion.getStatus());
            }

        }
    }

    private void addAwardNotificationStatus(final Map<Long, Set<String>> statusMap, final Long fiscaYearId) {
        List<AwardNotification> awardNotificationList = awardNotificationRepository.findAll();
        for (AwardNotification awardNotification : awardNotificationList) {
            if (awardNotification.getTenderQuotationEvaluation() != null
                    && awardNotification.getTenderQuotationEvaluation().getTender() != null
                    && awardNotification.getTenderQuotationEvaluation().getTender().getPurchaseRequisition() != null
                    && awardNotification.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                    .getProject() != null) {

                Long projectId = awardNotification.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                        .getProject().getId();
                addStatus(statusMap, projectId, awardNotification.getStatus());
            }
        }

    }

    private void addAwardAcceptanceStatus(final Map<Long, Set<String>> statusMap, final Long fiscaYearId) {
        List<AwardAcceptance> awardAcceptanceList = awardAcceptanceRepository.findAll();
        for (AwardAcceptance awardAcceptance : awardAcceptanceList) {
            if (awardAcceptance.getTenderQuotationEvaluation() != null
                    && awardAcceptance.getTenderQuotationEvaluation().getTender() != null
                    && awardAcceptance.getTenderQuotationEvaluation().getTender().getPurchaseRequisition() != null
                    && awardAcceptance.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                    .getProject() != null) {

                Long projectId = awardAcceptance.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                        .getProject().getId();
                addStatus(statusMap, projectId, awardAcceptance.getStatus());
            }
        }

    }

    private void addContractStatus(final Map<Long, Set<String>> statusMap, final Long fiscaYearId) {
        List<Contract> contractList = contractRepository.findAll();
        for (Contract contract : contractList) {
            if (contract.getTenderQuotationEvaluation() != null
                    && contract.getTenderQuotationEvaluation().getTender() != null
                    && contract.getTenderQuotationEvaluation().getTender().getPurchaseRequisition() != null && contract
                    .getTenderQuotationEvaluation().getTender().getPurchaseRequisition().getProject() != null) {

                Long projectId = contract.getTenderQuotationEvaluation().getTender().getPurchaseRequisition()
                        .getProject().getId();
                addStatus(statusMap, projectId, contract.getStatus());
            }
        }

    }


    private void addStatus(final Map<Long, Set<String>> statusMap, final Long projectId, final String status) {
        Set<String> statuses = statusMap.get(projectId);

        if (statuses == null) {
            statuses = new HashSet<>();
            statusMap.put(projectId, statuses);
        }

        statuses.add(status);
    }

}
