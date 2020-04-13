package org.devgateway.toolkit.persistence.service.overview;

import org.apache.commons.collections4.SetUtils;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.ProjectAttachable;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
import org.devgateway.toolkit.persistence.dto.StatusOverviewProjectStatus;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProjectFilterState;
import org.devgateway.toolkit.persistence.service.form.AbstractImplTenderProcessMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.AdministratorReportService;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.InspectionReportService;
import org.devgateway.toolkit.persistence.service.form.MEReportService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.persistence.service.form.PaymentVoucherService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.APPROVED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.SUBMITTED;

/**
 * @author gmutuhu
 */
@Service
@Transactional
public class StatusOverviewServiceImpl implements StatusOverviewService {
    protected static final Logger logger = LoggerFactory.getLogger(StatusOverviewService.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TenderProcessService tenderProcessService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private TenderQuotationEvaluationService tenderQuotationEvaluationService;

    @Autowired
    private ProfessionalOpinionService professionalOpinionService;

    @Autowired
    private AwardNotificationService awardNotificationService;

    @Autowired
    private AwardAcceptanceService awardAcceptanceService;

    @Autowired
    private AdministratorReportService administratorReportService;

    @Autowired
    private InspectionReportService inspectionReportService;

    @Autowired
    private PMCReportService pmcReportService;

    @Autowired
    private MEReportService meReportService;

    @Autowired
    private PaymentVoucherService paymentVoucherService;

    @Autowired
    private ContractService contractService;

    @Override
    @Transactional(readOnly = true)
    public List<StatusOverviewData> getAllProjects(final FiscalYear fiscalYear, final String projectTitle) {
        final List<Project> projects = projectService.findAll(
                new ProjectFilterState(fiscalYear, projectTitle).getSpecification());

        final Map<Project, List<String>> tenderStatusMap = addStatus(
                fiscalYear, tenderProcessService, tenderService,
                tenderQuotationEvaluationService, professionalOpinionService);
        final Map<Project, List<String>> awardStatusMap = addStatus(
                fiscalYear, awardNotificationService, awardAcceptanceService, contractService);

        final Map<Project, List<String>> implementationStatusMap = addImplementationStatus(
                fiscalYear, administratorReportService, inspectionReportService, pmcReportService, meReportService,
                paymentVoucherService);

        // get list of statuses of PurchaseRequisition forms grouped by Project
        final Map<Project, List<String>> purchaseStatusMap = groupStatusByProject(
                tenderProcessService.findByFiscalYear(fiscalYear));

        final List<StatusOverviewData> statusOverviewData = new ArrayList<>();
        for (final Project project : projects) {
            StatusOverviewData sod = statusOverviewData.stream()
                    .filter(item -> project.getProcurementPlan().getId().equals(item.getProcurementPlan().getId()))
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
                statusOverviewProjectStatus.setImplementationStatus(DBConstants.Status.NOT_STARTED);
            } else {
                statusOverviewProjectStatus.setTenderProcessStatus(
                        getProcessStatus(tenderStatusMap.get(project), purchaseStatuses.size() * 4));
                statusOverviewProjectStatus.setAwardProcessStatus(
                        getProcessStatus(awardStatusMap.get(project), purchaseStatuses.size() * 3));
                statusOverviewProjectStatus.setImplementationStatus(
                        getProcessStatus(implementationStatusMap.get(project), 0));
            }

            sod.getProjects().add(statusOverviewProjectStatus);
        }

        return statusOverviewData;
    }

    @Override
    public Long countProjects(FiscalYear fiscalYear, String projectTitle) {
        return projectService.count(new ProjectFilterState(fiscalYear, projectTitle).getSpecification());
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

            if (statuses.size() == 0 || (sizeCheck != 0 && statuses.size() != sizeCheck)) {
                return DBConstants.Status.NOT_STARTED;
            }

            if (statuses.contains(DBConstants.Status.DRAFT)) {
                return DBConstants.Status.DRAFT;
            }

            if (statuses.contains(DBConstants.Status.SUBMITTED)) {
                return DBConstants.Status.SUBMITTED;
            }

            if (statuses.contains(APPROVED)) {
                return APPROVED;
            }
        }

        return DBConstants.Status.NOT_STARTED;
    }

    /**
     * Group a list of {@link ProjectAttachable} objects by {@link Project} and collect all their status.
     */
    private <S extends ProjectAttachable & Statusable>
    Map<Project, List<String>> groupStatusByProject(final List<S> list) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(ProjectAttachable::getProject,
                        Collectors.mapping(key -> key.getStatus(), Collectors.toList())));
    }

    private Map<Project, List<String>> groupStatusByProjectPaymentVoucher(Map<Project, List<String>> statusMap,
                                                                          final List<PaymentVoucher> list) {
        Map<Project, List<String>> paymentStatus = list.parallelStream().filter(p -> !p.getStatus().equals(SUBMITTED))
                .collect(Collectors.groupingBy(ProjectAttachable::getProject,
                        Collectors.mapping(key -> key.getStatus().equals(APPROVED)
                                        ? (key.getLastPayment() ? APPROVED : SUBMITTED) : key.getStatus(),
                                Collectors.toList())));

        //if there are projects with no payment vouchers, consider the project at most submitted, never approved
        SetUtils.SetView<Project> difference = SetUtils.difference(statusMap.keySet(), paymentStatus.keySet());
        difference.forEach(i -> paymentStatus.put(i, Collections.singletonList(SUBMITTED)));

        return paymentStatus;
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
                                         final AbstractMakueniEntityService<? extends S>... services) {
        Map<Project, List<String>> statusMap = new HashMap<>();

        for (AbstractMakueniEntityService<? extends S> service : services) {
            final List<? extends S> list = service.findByFiscalYear(fiscalYear);
            statusMap = mergeMapOfStatuses(statusMap, groupStatusByProject(list));
        }

        return statusMap;
    }

    private <S extends AbstractImplTenderProcessMakueniEntity & ProjectAttachable & Statusable>
    Map<Project, List<String>> addImplementationStatus(final FiscalYear fiscalYear,
                                                       final AbstractImplTenderProcessMakueniEntityService
                                                               <? extends S>... services) {
        Map<Project, List<String>> statusMap = new HashMap<>();

        for (AbstractMakueniEntityService<? extends S> service : services) {
            final List<? extends S> list = service.findByFiscalYear(fiscalYear);
            statusMap = mergeMapOfStatuses(statusMap,
                    service instanceof PaymentVoucherService
                            ? groupStatusByProjectPaymentVoucher(statusMap, (List<PaymentVoucher>) list)
                            : groupStatusByProject(list));
        }

        return statusMap;
    }
}
