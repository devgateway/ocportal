package org.devgateway.ocds.web.ussd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.devgateway.ocds.web.spring.USSDService;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsSubscription;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;

/**
 * @author Octavian Ciubotaru
 */
public class UnsubscribePage extends Page {

    private final List<SubscriptionTarget> items;

    public UnsubscribePage(USSDService ussdService, USSDRequest request, Locale locale) {
        super(ussdService, request, locale);

        AlertsSubscription sub = ussdService.getAlertsSubscriptionService().getByMsisdn(request.msisdn);
        if (sub == null || (sub.getWards().isEmpty() && sub.getSubcounties().isEmpty())) {
            items = new ArrayList<>();
        } else {
            Stream<SubscriptionTarget> subcounties = sub.getSubcounties().stream().map(SubscriptionTarget::new);
            Stream<SubscriptionTarget> wards = sub.getWards().stream().map(SubscriptionTarget::new);
            items = Stream.concat(subcounties, wards)
                    .sorted(Comparator.comparing(SubscriptionTarget::getLabel))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public String getText() {
        if (items.isEmpty()) {
            return endConversation(getMessage("noSubscriptions"));
        } else {
            String options = "1. " + getMessage("all") + "\n" +
                    IntStream.range(0, items.size())
                            .mapToObj(i -> (i + 2) + ". " + items.get(i).getLabel())
                            .collect(Collectors.joining("\n"));
            return continueConversation(getMessage("unsubscribeMenu")) + "\n" + options;
        }
    }

    @Override
    public String processOption(int option) {
        if (option == 1) {
            ussdService.getAlertsSubscriptionService().unsubscribe(request.msisdn);
            return endConversation(getMessage("unsubscribeAllSuccessful"));
        } else if (option > 1 && (option - 2) < items.size()) {
            SubscriptionTarget item = items.get(option - 2);
            if (item.ward != null) {
                ussdService.getAlertsSubscriptionService().unsubscribe(request.msisdn, item.ward);
            } else {
                ussdService.getAlertsSubscriptionService().unsubscribe(request.msisdn, item.subcounty);
            }
            return endConversation(getMessage("unsubscribeSuccessful", item.label));
        } else {
            return endConversation(getMessage("invalidOption"));
        }
    }

    static class SubscriptionTarget {

        private String label;
        private Subcounty subcounty;
        private Ward ward;

        public SubscriptionTarget(Subcounty subcounty) {
            this.subcounty = subcounty;
            label = subcounty.getLabel();
        }
        public SubscriptionTarget(Ward ward) {
            this.ward = ward;
            label = ward.getSubcounty().getLabel() + " / " + ward.getLabel();
        }

        public String getLabel() {
            return label;
        }
    }
}
