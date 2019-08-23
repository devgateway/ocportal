package org.devgateway.toolkit.web.rest.controller.alerts;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author idobre
 * @since 23/08/2019
 */
@RestController
public class AlertsController {
    private static final Logger logger = LoggerFactory.getLogger(AlertsController.class);

    @Autowired
    private AlertService alertService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AlertsEmailService alertsEmailService;

    @ApiOperation(value = "Subscribe to a particular Email Alert")
    @RequestMapping(value = "/api/makueni/alerts/subscribeAlert",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    @Transactional
    public Boolean subscribeAlert(@ModelAttribute @Valid final AlertsRequest alertsRequest) {
        try {
            // we should subscribe to at least 1 Department or 1 Item
            if (CollectionUtils.isEmpty(alertsRequest.getDepartments())
                    && CollectionUtils.isEmpty(alertsRequest.getItems())) {
                return false;
            }

            final Set<Department> departments;
            final Set<Item> items;

            if (CollectionUtils.isNotEmpty(alertsRequest.getDepartments())) {
                departments = departmentService.findAllById(alertsRequest.getDepartments());
            } else {
                departments = new HashSet<>();
            }

            if (CollectionUtils.isNotEmpty(alertsRequest.getItems())) {
                items = itemService.findAllById(alertsRequest.getItems());
            } else {
                items = new HashSet<>();
            }

            final Alert alert = new Alert(alertsRequest.getEmail(), departments, items);
            final String secret = RandomStringUtils.randomAlphanumeric(32);
            alert.setSecret(secret);
            alert.setSecretValidUntil(LocalDateTime.now().plusDays(1));

            // save alert and send email verification message.
            alertService.saveAndFlush(alert);
            alertsEmailService.sendVerifyEmail(alert);

            return true;
        } catch (Exception e) {
            logger.error("Exception while subscribing to alerts", e);
            throw e;
        }
    }

    @ApiOperation(value = "Unsubscribe to a particular Email Alert")
    @RequestMapping(value = "/api/makueni/alerts/unsubscribeAlert",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public Boolean unsubscribeAlert() {

        return true;
    }
}
