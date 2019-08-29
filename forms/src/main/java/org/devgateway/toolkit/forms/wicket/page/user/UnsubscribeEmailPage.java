package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 29/08/2019
 */

@MountPath(value = "/unsubscribeEmail")
public class UnsubscribeEmailPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(UnsubscribeEmailPage.class);

    private final String secret;

    @SpringBean
    private AlertService alertService;

    public UnsubscribeEmailPage(final PageParameters parameters) {
        super(parameters);

        secret = parameters.get(0).toOptionalString();
        pageTitle.setVisibilityAllowed(false);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }
}
