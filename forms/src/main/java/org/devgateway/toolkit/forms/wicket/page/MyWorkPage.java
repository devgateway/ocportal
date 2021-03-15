package org.devgateway.toolkit.forms.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author Octavian Ciubotaru
 */
@MountPath
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class MyWorkPage extends BasePage {

    public MyWorkPage(PageParameters parameters) {
        super(parameters);

        add(new ListLockPanel("list", true));
    }
}
