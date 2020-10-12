package org.devgateway.ocds.web.spring;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.ocds.web.rest.controller.USSDController;
import org.devgateway.ocds.web.ussd.MainMenuPage;
import org.devgateway.ocds.web.ussd.USSDRequest;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.service.alerts.AlertsSubscriptionService;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class USSDService {

    public static final Locale[] LOCALES = new Locale[] { Locale.ENGLISH, new Locale("sw") };

    private final USSDProperties ussdProperties;

    private final MessageSource messageSource;

    private final SubcountyService subcountyService;

    private final WardService wardService;

    private final AlertsSubscriptionService alertsSubscriptionService;

    public USSDService(
            USSDProperties ussdProperties,
            @Qualifier("ussd") MessageSource messageSource,
            SubcountyService subcountyService,
            WardService wardService,
            AlertsSubscriptionService alertsSubscriptionService) {
        this.ussdProperties = ussdProperties;
        this.messageSource = messageSource;
        this.subcountyService = subcountyService;
        this.wardService = wardService;
        this.alertsSubscriptionService = alertsSubscriptionService;
    }

    public String process(USSDController.USSDMessage message) {
        if (!message.getServiceCode().equals(ussdProperties.getServiceCode())) {
            throw new RuntimeException(String.format("Unknown USSD service code %s. Expected: %s.",
                    message.getServiceCode(), ussdProperties.getServiceCode()));
        }

        String[] basePath = StringUtils.split(ussdProperties.getBaseString(), '*');
        String[] fullPath = StringUtils.split(message.getUssdString(), '*');

        String[] relativePath = relativePath(basePath, fullPath);

        USSDRequest request = new USSDRequest(message.getMsisdn(), relativePath);

        return MainMenuPage.from(this, request).respond();
    }

    private String[] relativePath(String[] basePath, String[] fullPath) {
        if (basePath.length > fullPath.length) {
            throw new RuntimeException();
        }

        for (int i = 0; i < basePath.length; i++) {
            if (!basePath[i].equals(fullPath[i])) {
                throw new RuntimeException();
            }
        }

        return Arrays.copyOfRange(fullPath, basePath.length, fullPath.length);
    }

    public String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(code, args, locale);
    }

    public List<Subcounty> getSubcounties() {
        return subcountyService.findAll();
    }

    public List<Ward> getWards() {
        return wardService.findAll();
    }

    public AlertsSubscriptionService getAlertsSubscriptionService() {
        return alertsSubscriptionService;
    }
}
