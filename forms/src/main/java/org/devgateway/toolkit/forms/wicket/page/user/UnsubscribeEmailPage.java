package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 29/08/2019
 * <p>
 * Unsubscribe an user from an {@link Alert}.
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

        // check if we have any errors
        final Boolean error;
        if (secret == null) {
            error = true;
        } else {
            final Alert alert = alertService.findBySecret(secret);

            if (alert == null) {
                error = true;
            } else {
                error = false;

                alertService.unsubscribeAlert(alert);
            }
        }

        final TransparentWebMarkupContainer messageContainer = new TransparentWebMarkupContainer("messageContainer");
        add(messageContainer);
        final TransparentWebMarkupContainer errorContainer = new TransparentWebMarkupContainer("errorContainer");
        add(errorContainer);

        errorContainer.setVisibilityAllowed(error);
        messageContainer.setVisibilityAllowed(!error);
    }
}
