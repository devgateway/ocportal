package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.devgateway.toolkit.web.security.SecurityConstants;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class CorruptionRiskDashboardPage extends WebPage {
    public static final String URL = "/ui/index.html#!/crd";

    public CorruptionRiskDashboardPage() {
        throw new RedirectToUrlException(URL);
    }
}
