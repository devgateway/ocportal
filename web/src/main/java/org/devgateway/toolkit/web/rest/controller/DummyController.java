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

import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.devgateway.toolkit.persistence.service.category.ProcurementMethodService;
import org.devgateway.toolkit.persistence.service.category.TargetGroupService;
import org.devgateway.toolkit.persistence.service.form.PlanItemService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.rest.entity.Dummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                item.setDescription("Description " + (currentSize + i));
                item.setEstimatedCost(100.0);
                item.setUnitOfIssue("unit of issue....");
                item.setQuantity(1001);
                item.setUnitPrice(200.34);
                item.setTotalCost(2295000.503);
                item.setProcurementMethod(procurementMethods.get(random.nextInt(procurementMethods.size())));
                item.setSourceOfFunds("dk20fk0-2-ck-sk93-0001");
                item.setTargetGroup(targetGroups.get(random.nextInt(targetGroups.size())));
                item.setTargetGroupValue(76.12);
                item.setQuarter1st(12.0);
                item.setQuarter2nd(34.0);
                item.setQuarter3rd(56.0);
                item.setQuarter4th(78.0);

                planItemService.save(item);
                planItems.add(item);
            }

            procurementPlanService.saveAndFlush(pp);

        } else {
            return "No ProcurementPlan with the id: " + id + " found!";
        }

        return "Success!";
    }
}