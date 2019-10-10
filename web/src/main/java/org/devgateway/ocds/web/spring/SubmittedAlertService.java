package org.devgateway.ocds.web.spring;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class SubmittedAlertService {

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AwardAcceptanceService awardAcceptanceService;

    @Autowired
    private AwardNotificationService awardNotificationService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProfessionalOpinionService professionalOpinionService;

    @Autowired
    private PurchaseRequisitionService purchaseRequisitionService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private TenderQuotationEvaluationService tenderQuotationEvaluationService;

    //create map with Department, Type, ID, Title
    private Map<Long, Map<Class<? extends AbstractMakueniEntity>, Map<Long, String>>> departmentTypeIdTitle;

    private List<? extends AbstractMakueniEntityService> services;


    @PostConstruct
    public void init() {
        departmentTypeIdTitle = new ConcurrentHashMap<>();

        services = Collections.unmodifiableList(Arrays.asList(
                projectService, //not pr
                awardAcceptanceService,
                awardNotificationService,
                contractService,
                procurementPlanService, //not pr
                professionalOpinionService,
                purchaseRequisitionService, //not pr
                tenderService,
                tenderQuotationEvaluationService));

        collectDepartmentTypeIdTitle();
    }

    @PreDestroy
    public void destroy() {
        departmentTypeIdTitle = null;
        services = null;
    }

    @Transactional
    private void collectDepartmentTypeIdTitle() {

        try (Stream<? extends AbstractMakueniEntity> stream = projectService.getAllSubmitted()) {
            stream.forEach(
                    o -> {
                        AbstractMakueniEntity e = (AbstractMakueniEntity) o;
                        Department department = e.getDepartment();
                        departmentTypeIdTitle.putIfAbsent(department.getId(), new ConcurrentHashMap<>());
                        Map<Class<? extends AbstractMakueniEntity>, Map<Long, String>> departmentMap = departmentTypeIdTitle.get(department.getId());
                        departmentMap.putIfAbsent(e.getClass(), new ConcurrentHashMap<>());
                        Map<Long, String> typeMap = departmentMap.get(e.getClass());
                        typeMap.put(e.getId(), e.getLabel());
                    }
            );
            System.out.println(departmentTypeIdTitle);
        }
    }


}
