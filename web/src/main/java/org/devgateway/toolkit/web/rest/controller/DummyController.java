/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.web.rest.controller;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.PlanItemService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.rest.entity.Dummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mpostelnicu
 */
@RestController
public class DummyController {
    @Autowired
    private ProcurementPlanService procurementPlanService;

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
    private ContractService contractService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FiscalYearService fiscalYearService;

    @Autowired
    private PlanItemService planItemService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ProcurementMethodService procurementMethodService;

    @Autowired
    private TargetGroupService targetGroupService;

    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/dummy")
    public Dummy greeting(@RequestParam(value = "name", defaultValue = "World") final String name) {
        return new Dummy(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

    @RequestMapping("/generatePP")
    public String generatePP(@RequestParam(value = "id", defaultValue = "0") final Long id) {
        if (id == 0) {
            return "no id";
        }

        final Optional<ProcurementPlan> procurementPlanOptional = procurementPlanService.findById(id);
        if (procurementPlanOptional.isPresent()) {
            final ProcurementPlan pp = procurementPlanOptional.get();
            final List<PlanItem> planItems = pp.getPlanItems();
            final int currentSize = planItems.size();

            final Random random = new Random();

            final List<Item> items = itemService.findAll();
            final List<ProcurementMethod> procurementMethods = procurementMethodService.findAll();
            final List<TargetGroup> targetGroups = targetGroupService.findAll();

            // generate 100 PlanItems
            for (int i = 0; i < 100; i++) {
                final PlanItem item = new PlanItem();
                item.setItem(items.get(random.nextInt(items.size())));
                item.setEstimatedCost(new BigDecimal(100.0));
                item.setQuantity(new BigDecimal(1001));
                item.setProcurementMethod(procurementMethods.get(random.nextInt(procurementMethods.size())));
                item.setSourceOfFunds("dk20fk0-2-ck-sk93-0001");
                item.setTargetGroup(targetGroups.get(random.nextInt(targetGroups.size())));
                item.setTargetGroupValue(new BigDecimal(76.12));
                item.setQuarter1st(new BigDecimal(12.0));
                item.setQuarter2nd(new BigDecimal(34.0));
                item.setQuarter3rd(new BigDecimal(56.0));
                item.setQuarter4th(new BigDecimal(78.0));

                planItemService.save(item);
                planItems.add(item);
            }

            procurementPlanService.saveAndFlush(pp);

        } else {
            return "No ProcurementPlan with the id: " + id + " found!";
        }

        return "Success!";
    }

    @RequestMapping("/generateTest")
    @Transactional
    public String generateTestStress(@RequestParam(value = "generate", defaultValue = "no") final String generate) {
        if (generate.equals("ok")) {
            final Random random = new Random();

            final FiscalYear fiscalYear = new FiscalYear();
            final int year = random.nextInt(3000);
            fiscalYear.setName(year + "/" + (year + 1));
            fiscalYear.setStartDate(new Date());
            fiscalYear.setEndDate(new Date());

            fiscalYearService.save(fiscalYear);

            final List<Department> departments = departmentService.findAll();

            final List<ProcurementPlan> procurementPlans = new ArrayList<>();
            final List<Project> projects = new ArrayList<>();
            final List<TenderProcess> prs = new ArrayList<>();
            final List<Tender> tenders = new ArrayList<>();
            final List<TenderQuotationEvaluation> tenderQuotationEvaluations = new ArrayList<>();
            final List<ProfessionalOpinion> professionalOpinions = new ArrayList<>();
            final List<AwardNotification> awardNotifications = new ArrayList<>();
            final List<AwardAcceptance> awardAcceptances = new ArrayList<>();
            final List<Contract> contracts = new ArrayList<>();

            for (final Department department : departments) {
                final ProcurementPlan procurementPlan = new ProcurementPlan();
                procurementPlan.setDepartment(department);
                procurementPlan.setFiscalYear(fiscalYear);
                procurementPlan.setApprovedDate(new Date());

                procurementPlans.add(procurementPlan);

                for (int i = 0; i < 3; i++) {
                    final Project project = new Project();
                    project.setProcurementPlan(procurementPlan);
                    project.setProjectTitle("Project " + i);
                    project.setAmountBudgeted(new BigDecimal(100.0));
                    project.setAmountRequested(new BigDecimal(200.0));

                    projects.add(project);

                    for (int j = 0; j < 3; j++) {
                        final TenderProcess tenderProcess = new TenderProcess();
                        tenderProcess.setProject(project);
                        tenderProcess.setPurchaseRequestNumber("# " + i + j + "-" + random.nextInt(50000));

                        prs.add(tenderProcess);

                        final Tender tender = new Tender();
                        tender.setTenderProcess(tenderProcess);
                        tender.setTenderTitle("Tender " + i + j + "-" + random.nextInt(50000));
                        tenders.add(tender);

                        final TenderQuotationEvaluation tenderQuotationEvaluation = new TenderQuotationEvaluation();
                        tenderQuotationEvaluation.setTenderProcess(tenderProcess);
                        tenderQuotationEvaluations.add(tenderQuotationEvaluation);

                        final ProfessionalOpinion professionalOpinion = new ProfessionalOpinion();
                        professionalOpinion.setTenderProcess(tenderProcess);
                        professionalOpinions.add(professionalOpinion);

                        final AwardNotification awardNotification = new AwardNotification();
                        awardNotification.setTenderProcess(tenderProcess);
                        awardNotifications.add(awardNotification);

                        final AwardAcceptance awardAcceptance = new AwardAcceptance();
                        awardAcceptance.setTenderProcess(tenderProcess);
                        awardAcceptances.add(awardAcceptance);

                        final Contract contract = new Contract();
                        contract.setTenderProcess(tenderProcess);
                        contracts.add(contract);
                    }
                }
            }

            procurementPlanService.saveAll(procurementPlans);
            projectService.saveAll(projects);
            tenderProcessService.saveAll(prs);
            tenderService.saveAll(tenders);
            tenderQuotationEvaluationService.saveAll(tenderQuotationEvaluations);
            professionalOpinionService.saveAll(professionalOpinions);
            awardNotificationService.saveAll(awardNotifications);
            awardAcceptanceService.saveAll(awardAcceptances);
            contractService.saveAll(contracts);

            // clear cache here from browser instead of adding a dependency to form module.

            return "Success!";
        }

        return "Doing Nothing!";
    }
}