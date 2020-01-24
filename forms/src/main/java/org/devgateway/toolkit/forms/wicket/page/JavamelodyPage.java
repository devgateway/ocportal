package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.devgateway.toolkit.web.security.SecurityConstants;


/**
 * @author mpostelnicu
 * <p>
 * Simple class the redirect the user to javamelody UI URL (which is an external Wicket URL)
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class JavamelodyPage extends RedirectPage {
    public static final String URL = "/monitoring";

    public JavamelodyPage() {
        super(URL);
    }
}
