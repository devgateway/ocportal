package org.devgateway.ocds.web.ussd;

import java.util.Locale;

import org.devgateway.ocds.web.spring.USSDService;

/**
 * @author Octavian Ciubotaru
 */
public class ChooseLanguagePage extends Page {

    public ChooseLanguagePage(USSDService ussdService, USSDRequest request, Locale locale) {
        super(ussdService, request, locale);
    }

    @Override
    public String getText() {
        return continueConversation(getMessage("selectLanguage"));
    }

    @Override
    public String processOption(int option) {
        if (option > 0 || (option - 1) < USSDService.LOCALES.length) {
            Locale locale = USSDService.LOCALES[option - 1];
            ussdService.getAlertsSubscriptionService().changeLanguage(request.msisdn, locale.getLanguage());
            return endConversation(getMessage(locale, "languageChangedSuccessfully"));
        } else {
            return endConversation(getMessage("invalidOption"));
        }
    }
}
