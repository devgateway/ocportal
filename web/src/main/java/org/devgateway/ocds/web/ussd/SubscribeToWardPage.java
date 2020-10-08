package org.devgateway.ocds.web.ussd;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.devgateway.ocds.web.spring.USSDService;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;

/**
 * @author Octavian Ciubotaru
 */
public class SubscribeToWardPage extends Page {

    private final Subcounty subcounty;
    private final List<Ward> wards;

    public SubscribeToWardPage(USSDService ussdService, USSDRequest request, Locale locale, Subcounty subcounty) {
        super(ussdService, request, locale);
        this.subcounty = subcounty;
        wards = ussdService.getWards().stream()
                .filter(w -> w.getSubcounty().equals(subcounty))
                .sorted(Comparator.comparing(Category::getLabel))
                .collect(Collectors.toList());
    }

    @Override
    public String getText() {
        String options = "1. " + getMessage("allWards") + "\n" +
                IntStream.range(0, wards.size())
                        .mapToObj(i -> (i + 2) + ". " + wards.get(i).getLabel())
                        .collect(Collectors.joining("\n"));
        String message = getMessage("subscribeToWardMenu") + "\n" + options;
        return continueConversation(message);
    }

    @Override
    public String processOption(int selectedOption) {
        if (selectedOption == 1) {
            ussdService.getAlertsSubscriptionService().subscribe(request.msisdn, subcounty);
            return endConversation(getMessage("subscriptionSuccessful", subcounty.getLabel()));
        } else if ((selectedOption - 2) < wards.size()) {
            Ward ward = wards.get(selectedOption - 2);
            ussdService.getAlertsSubscriptionService().subscribe(request.msisdn, ward);
            return endConversation(getMessage("subscriptionSuccessful", ward.getLabel()));
        } else {
            return endConversation(getMessage("invalidOption"));
        }
    }
}
