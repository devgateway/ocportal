package org.devgateway.ocds.web.ussd;

import java.util.Locale;

import org.devgateway.ocds.web.spring.USSDService;
import org.springframework.context.NoSuchMessageException;

/**
 * @author Octavian Ciubotaru
 */
public abstract class Page {

    /**
     * Used to communicate that USSD session must continue.
     */
    private static final String CON = "CON";

    /**
     * Used to communicate that USSD session must end.
     */
    private static final String END = "END";

    final USSDService ussdService;
    final USSDRequest request;
    final Locale locale;

    public Page(USSDService ussdService, USSDRequest request, Locale locale) {
        this.ussdService = ussdService;
        this.request = request;
        this.locale = locale;
    }

    public final String respond() {
        if (request.path.length == 0) {
            return getText();
        } else {
            try {
                int option = Integer.parseInt(request.path[0]);
                return processOption(option);
            } catch (NumberFormatException e) {
                return endConversation(getMessage("invalidOption"));
            }
        }
    }

    public abstract String getText();

    public abstract String processOption(int option);

    protected String continueConversation(String message) {
        return CON + " " + message;
    }

    protected String endConversation(String message) {
        return END + " " + message;
    }

    protected String getMessage(String code, Object... args) throws NoSuchMessageException {
        return getMessage(locale, code, args);
    }

    protected String getMessage(Locale locale, String code, Object... args) throws NoSuchMessageException {
        return ussdService.getMessage(code, args, locale);
    }
}
