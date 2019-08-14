package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.devgateway.toolkit.web.security.SecurityConstants;

/**
 * @author idobre
 * @since 2019-07-11
 *
 * Simple class the redirect the user to Public Portal App (which is an external Wicket URL)
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class PublicPortalPage extends WebPage {
    public static final String URL = "/ui/index.html#!/m-and-e";

    public PublicPortalPage() {
        throw new RedirectToUrlException(URL);
    }
}
