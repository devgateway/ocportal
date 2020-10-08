package org.devgateway.ocds.web.ussd;

import java.util.Locale;
import java.util.stream.Stream;

import org.devgateway.ocds.web.spring.USSDService;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsSubscription;

/**
 * @author Octavian Ciubotaru
 */
public class MainMenuPage extends Page {

    public static MainMenuPage from(USSDService ussdService, USSDRequest request) {
        AlertsSubscription sub = ussdService.getAlertsSubscriptionService().getByMsisdn(request.msisdn);
        Locale locale = Stream.of(USSDService.LOCALES)
                .filter(l -> sub != null && l.getLanguage().equals(sub.getLanguage()))
                .findFirst()
                .orElse(USSDService.LOCALES[0]);
        return new MainMenuPage(ussdService, request, locale);
    }

    private MainMenuPage(USSDService ussdService, USSDRequest request, Locale locale) {
        super(ussdService, request, locale);
    }

    @Override
    public String getText() {
        return continueConversation(getMessage("mainMenu"));
    }

    @Override
    public String processOption(int option) {
        if (option == 1) {
            return new SubscribePage(ussdService, request.shift(), locale).respond();
        } else if (option == 2) {
            return new UnsubscribePage(ussdService, request.shift(), locale).respond();
        } else if (option == 3) {
            return new ChooseLanguagePage(ussdService, request.shift(), locale).respond();
        } else {
            return endConversation(getMessage("invalidOption"));
        }
    }
}
