package org.devgateway.toolkit.persistence.service.overview;

import org.apache.commons.collections4.SetUtils;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.*;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowGroup;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowInfo;
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
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.APPROVED;
import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.SUBMITTED;

/**
 * @author gmutuhu
 */
@Service
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
    public List<StatusOverviewRowGroup> getDisplayableTenderProcesses(FiscalYear fiscalYear, String title) {
        List<StatusOverviewRowGroup> groupList = new ArrayList<>();
        Map<ProcurementPlan, List<TenderProcess>> pptenderProcesses = tenderProcessService.findAll((r, cq, cb) -> cb.and(
                cb.equal(r.join(TenderProcess_.procurementPlan).get(ProcurementPlan_.fiscalYear), fiscalYear),
                cb.isNull(r.get(TenderProcess_.project)),
                ObjectUtils.isEmpty(title) ? cb.and() : cb.like(
                        cb.lower(r.join(TenderProcess_.tender).get(Tender_.tenderTitle)),
                        "%" + title.toLowerCase() + "%")
        )).stream().collect(Collectors.groupingBy(TenderProcess::getProcurementPlan));

        pptenderProcesses.forEach((procurementPlan, tenderProcesses) -> {
            StatusOverviewRowGroup group = new StatusOverviewRowGroup();
            groupList.add(group);
            group.setProcurementPlan(procurementPlan);
            tenderProcesses.forEach(tp -> {
                StatusOverviewRowInfo rowInfo = new StatusOverviewRowInfo();
                group.getRows().add(rowInfo);
                rowInfo.setId(tp.getId());
                rowInfo.setTitle(tp.getSingleTender() == null ? "No title" : tp.getSingleTender().getTitle());
                rowInfo.setTenderProcessStatus(
                        getProcessStatus(getMakueniEntitiesStatuses(
                                Collections.singletonList(tp),
                                tp.getTender(),
                                tp.getTenderQuotationEvaluation(), tp.getProfessionalOpinion()), 4));
                rowInfo.setAwardProcessStatus(
                        getProcessStatus(getMakueniEntitiesStatuses(tp.getAwardNotification(), tp.getAwardAcceptance(),
                                tp.getContract()), 3));
                rowInfo.setImplementationStatus(
                        getProcessStatus(getMakueniEntitiesStatuses(tp.getAdministratorReports(),
                                tp.getInspectionReports(), tp.getPmcReports(), tp.getMeReports(), tp.getPaymentVouchers()),
                                0));
            });

        });

        return groupList;
    }


    private <S extends AbstractMakueniEntity> List<String> getMakueniEntitiesStatuses(
            Collection<? extends S>... cols) {
        ArrayList<String> ret = new ArrayList<>();
        for (Collection<? extends S> col : cols) {
            col.forEach(e -> ret.add(getMakueniEntityVirtualStatus(e)));
        }
        return ret;
    }

    private String getMakueniEntityVirtualStatus(AbstractMakueniEntity e) {
        if (e instanceof PaymentVoucher) {
            return getPaymentVoucherVirtualStatus((PaymentVoucher) e);
        }
        return e.getStatus();
    }

    private String getPaymentVoucherVirtualStatus(PaymentVoucher pv) {
        if (pv.getStatus().equals(APPROVED) && !pv.getLastPayment()) {
            return SUBMITTED;
        }
        return pv.getStatus();
    }


    @Override
    @Transactional(readOnly = true)
    public List<StatusOverviewRowGroup> getAllProjects(final FiscalYear fiscalYear, final String projectTitle) {
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

        final List<StatusOverviewRowGroup> statusOverviewData = new ArrayList<>();
        for (final Project project : projects) {
            StatusOverviewRowGroup sod = statusOverviewData.stream()
                    .filter(item -> project.getProcurementPlan().getId().equals(item.getProcurementPlan().getId()))
                    .findFirst()
                    .orElse(null);
            if (sod == null) {
                sod = new StatusOverviewRowGroup();
                sod.setProcurementPlan(project.getProcurementPlan());
                statusOverviewData.add(sod);
            }

            final List<String> purchaseStatuses = purchaseStatusMap.get(project);

            final StatusOverviewRowInfo statusOverviewRowInfo = new StatusOverviewRowInfo();
            statusOverviewRowInfo.setId(project.getId());
            statusOverviewRowInfo.setTitle(project.getProjectTitle());
            statusOverviewRowInfo.setProjectStatus(project.getStatus());

            // check if we have at least one Purchase Requisition
            if (purchaseStatuses == null || purchaseStatuses.isEmpty()) {
                statusOverviewRowInfo.setTenderProcessStatus(DBConstants.Status.NOT_STARTED);
                statusOverviewRowInfo.setAwardProcessStatus(DBConstants.Status.NOT_STARTED);
                statusOverviewRowInfo.setImplementationStatus(DBConstants.Status.NOT_STARTED);
            } else {
                statusOverviewRowInfo.setTenderProcessStatus(
                        getProcessStatus(tenderStatusMap.get(project), purchaseStatuses.size() * 4));
                statusOverviewRowInfo.setAwardProcessStatus(
                        getProcessStatus(awardStatusMap.get(project), purchaseStatuses.size() * 3));
                statusOverviewRowInfo.setImplementationStatus(
                        getProcessStatus(implementationStatusMap.get(project), 0));
            }

            sod.getRows().add(statusOverviewRowInfo);
        }

        return statusOverviewData;
    }

    @Override
    public Long countProjects(FiscalYear fiscalYear, String projectTitle) {
        return projectService.count(new ProjectFilterState(fiscalYear, projectTitle).getSpecification());
    }

    @Override
    public Long countTenderProcesses(FiscalYear fiscalYear, String title) {
        return tenderProcessService.count();
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
    Map<Project, List<String>> groupStatusByProject(final Collection<S> list) {
        return list.parallelStream().filter(pa -> !ObjectUtils.isEmpty(pa.getProject()))
                .collect(Collectors.groupingBy(ProjectAttachable::getProject,
                        Collectors.mapping(key -> key.getStatus(), Collectors.toList())));
    }

    private Map<Project, List<String>> groupStatusByProjectPaymentVoucher(Map<Project, List<String>> statusMap,
                                                                          final List<PaymentVoucher> list) {
        Map<Project, List<String>> paymentStatus = list.parallelStream().filter(p -> !p.getStatus().equals(SUBMITTED)
                && !ObjectUtils.isEmpty(p.getProject()))
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
     * Merge 2 {@link Map} of statuses grouped by {@link S}.
     */
    private <S> Map<S, List<String>> mergeMapOfStatuses(final Map<S, List<String>> map1,
                                                        final Map<S, List<String>> map2) {
        final Map<S, List<String>> mergedMap = new HashMap<>(map1);
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
