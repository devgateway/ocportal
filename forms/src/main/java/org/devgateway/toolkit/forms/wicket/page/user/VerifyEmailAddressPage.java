package org.devgateway.toolkit.forms.wicket.page.user;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.Homepage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 23/08/2019
 *
 * <p>
 * Users can use this page to verify their email address(no need for the user to be authenticated).
 */
@MountPath(value = "/verifyEmail")
public class VerifyEmailAddressPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(VerifyEmailAddressPage.class);

    @SpringBean
    private AlertService alertService;

    private final String secret;

    private Label verifyEmailTime;

    private int timer = 10; // wait for 10 seconds before redirect

    public VerifyEmailAddressPage(final PageParameters parameters) {
        super(parameters);

        pageTitle.setVisibilityAllowed(false);

        secret = parameters.get(0).toOptionalString();
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

                alert.setAlertable(true);
                alert.setEmailVerified(true);
                alertService.saveAndFlush(alert);
            }
        }

        final TransparentWebMarkupContainer messageContainer = new TransparentWebMarkupContainer("messageContainer");
        add(messageContainer);
        final TransparentWebMarkupContainer errorContainer = new TransparentWebMarkupContainer("errorContainer");
        add(errorContainer);

        errorContainer.setVisibilityAllowed(error);
        messageContainer.setVisibilityAllowed(!error);

        final Label verifyEmailMsg = new Label("verifyEmailMsg",
                new StringResourceModel("verifyEmailMsg", VerifyEmailAddressPage.this));
        verifyEmailMsg.setEscapeModelStrings(false);
        messageContainer.add(verifyEmailMsg);

        final Label linkMsg = new Label("linkMsg",
                new StringResourceModel("linkMsg", VerifyEmailAddressPage.this));
        linkMsg.setEscapeModelStrings(false);
        messageContainer.add(linkMsg);

        verifyEmailTime = new Label("verifyEmailTime",
                new StringResourceModel("verifyEmailTime", VerifyEmailAddressPage.this)
                        .setParameters(timer));
        verifyEmailTime.setOutputMarkupId(true);
        messageContainer.add(verifyEmailTime);

        if (!error) {
            add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
                protected void onTimer(final AjaxRequestTarget target) {
                    // redirect to home page
                    if (timer == 1) {
                        setResponsePage(Homepage.class);
                        throw new RedirectToUrlException("/ui/index.html");
                    } else {
                        // decrease timer and update the label on the screen
                        timer--;
                        verifyEmailTime.setDefaultModel(
                                new StringResourceModel("verifyEmailTime", VerifyEmailAddressPage.this)
                                        .setParameters(timer));
                        target.add(verifyEmailTime);
                    }
                }
            });
        }
    }
}
