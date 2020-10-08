package org.devgateway.ocds.web.ussd;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.devgateway.ocds.web.spring.USSDService;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;

/**
 * @author Octavian Ciubotaru
 */
public class SubscribePage extends Page {

    private final List<Subcounty> subcounties;

    public SubscribePage(USSDService ussdService, USSDRequest request, Locale locale) {
        super(ussdService, request, locale);
        subcounties = ussdService.getSubcounties().stream()
                .sorted(Comparator.comparing(Category::getLabel))
                .collect(Collectors.toList());
    }

    @Override
    public String getText() {
        String options = IntStream.range(0, subcounties.size())
                .mapToObj(i -> (i + 1) + ". " + subcounties.get(i).getLabel())
                .collect(Collectors.joining("\n"));
        String message = getMessage("subscribeMenu") + "\n" + options;
        return continueConversation(message);
    }

    @Override
    public String processOption(int option) {
        if (option > 0 && (option - 1) < subcounties.size()) {
            Subcounty subcounty = subcounties.get(option - 1);
            return new SubscribeToWardPage(ussdService, request.shift(), locale, subcounty).respond();
        } else {
            return endConversation(getMessage("invalidOption"));
        }
    }
}
